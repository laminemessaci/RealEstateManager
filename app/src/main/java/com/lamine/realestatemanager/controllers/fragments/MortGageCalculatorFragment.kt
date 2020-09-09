package com.lamine.realestatemanager.controllers.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.controllers.activities.MainActivity
import com.lamine.realestatemanager.utils.Prefs
import com.lamine.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.fragment_mort_gage_calculator.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow


class MortGageCalculatorFragment : Fragment() {

    private var purchaseAmount = 0.0
    private var contribution = 0.0
    private var interestRate = 0.0
    private var value = 0.0
    private var duration = 1
    private var isDollars: Boolean = true
    private var monthlyEuro = ""
    private var totalEuro = ""
    private lateinit var currencyFormat: NumberFormat
    private lateinit var prefs: Prefs
    private var foreign: Boolean = false

    companion object {
        fun newInstance(): MortGageCalculatorFragment {
            return MortGageCalculatorFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mort_gage_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.calculator)
        getForeign()
        initViews()
    }

    private fun getForeign() {
        prefs = Prefs.get(RealEstateManagerApplication.getContext())
        foreign = prefs.getForeignCurrency()
        currencyFormat =
            if (foreign) NumberFormat.getCurrencyInstance(Locale.FRANCE) else NumberFormat.getCurrencyInstance(
                Locale.US
            )
        if (foreign) {
            isDollars = false
        }
    }

    private fun initViews() {
        editTextPurchasePrice.addTextChangedListener(
            getEditableTextWatcher(
                textViewPurchasePrice,
                "PP"
            )
        )
        editTextDownPaymentAmount.addTextChangedListener(
            getEditableTextWatcher(
                textViewDownPaymentAmount,
                "DPA"
            )
        )
        editTextInterestRate.addTextChangedListener(
            getEditableTextWatcher(
                textViewInterestRate,
                "IR"
            )
        )

        loan_seekBar.setOnSeekbarChangeListener { minValue ->
            textViewDuration.text = minValue.toString()
            val str: String = textViewDuration.text.toString()
            duration = str.toInt()
        }

        calculate.setOnClickListener {
            val dialog = AppCompatDialog(context)
            dialog.setContentView(R.layout.dialog)
            dialog.setTitle("Result")

            val monthlyPaymentTV =
                dialog.findViewById<View>(R.id.monthly_payment) as TextView?
            val totalPaymentTV =
                dialog.findViewById<View>(R.id.total_payment) as TextView?
            val monthlyStr: Double = getMonthlyPayment(
                interestRate,
                purchaseAmount,
                contribution,
                duration
            )
            monthlyPaymentTV?.text =
                currencyFormat.format(monthlyStr)

            val totalStr: Double = getTotalPayment(monthlyStr, duration)
            totalPaymentTV?.text =
                currencyFormat.format(totalStr)


            val dialogBtnConvert =
                dialog.findViewById<View>(R.id.dialogButtonConvert) as Button?
            dialogBtnConvert!!.setOnClickListener {
                if (isDollars) {
                    currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
                    dialogBtnConvert.setText(R.string.convertDollar)
                    if (monthlyEuro.isEmpty()) {
                        monthlyEuro = (Utils.convertDollarToEuro(monthlyStr)).toString()
                        totalEuro = (Utils.convertDollarToEuro(totalStr)).toString()
                    }
                    monthlyPaymentTV?.text = currencyFormat.format(monthlyEuro.toDouble())
                    totalPaymentTV?.text = currencyFormat.format(totalEuro.toDouble())

                    isDollars = false
                } else {
                    if (monthlyEuro.isEmpty()) {
                        monthlyEuro = monthlyStr.toString()
                        totalEuro = totalStr.toString()
                    }
                    currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
                    monthlyPaymentTV?.text =
                        currencyFormat.format(
                            Utils.convertEuroToDollar(monthlyEuro.toDouble())
                        )
                    totalPaymentTV?.text =
                        currencyFormat.format(
                            Utils.convertEuroToDollar(totalEuro.toDouble())
                        )
                    dialogBtnConvert.setText(R.string.convertEuro)
                    isDollars = true
                }


            }


            val dialogButtonOk =
                dialog.findViewById<View>(R.id.dialogButtonOK) as Button?
            dialogButtonOk!!.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }


    }


    // Get textWatcher
    private fun getEditableTextWatcher(textView: TextView, type: String): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                try {
                    value = s.toString().toDouble() / 100.0
                    when (type) {
                        "PP" -> {
                            purchaseAmount = value
                            textView.text = currencyFormat.format(value)
                        }
                        "DPA" -> {
                            contribution = value
                            textView.text = currencyFormat.format(value)
                        }
                        "IR" -> {
                            interestRate = value
                            textView.text = value.toString()
                        }
                    }
                } catch (e: NumberFormatException) {
                    textView.text = ""
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    // To calculate monthly payment
    fun getMonthlyPayment(
        interestRate: Double,
        purchaseAmount: Double,
        contribution: Double,
        duration: Int
    ): Double {
        val monthlyInterestRate: Double = interestRate / 1200

        return (purchaseAmount - contribution) * monthlyInterestRate /
                (1 - (1 / (1 + monthlyInterestRate).pow(duration * 12.toDouble())))
    }

    // To calculate total
    fun getTotalPayment(monthlyPayment: Double, duration: Int): Double {
        return monthlyPayment * duration * 12
    }

}
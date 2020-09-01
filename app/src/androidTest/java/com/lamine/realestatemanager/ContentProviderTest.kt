package com.lamine.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.telephony.TelephonyManager
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lamine.realestatemanager.database.RealEstateDatabase
import com.lamine.realestatemanager.models.Address
import com.lamine.realestatemanager.models.GeocodeInfo
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.CreateEstateUtils
import com.lamine.realestatemanager.utils.RealEstateStream
import com.lamine.realestatemanager.utils.Utils
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContentProviderTest {
    // FOR DATA
    private var mContentResolver: ContentResolver? = null
    private val authority = "com.lamine.openclassrooms.realestatemanager.provider"
    private val tableName = Property::class.java.simpleName
    private val uriProperty: Uri = Uri.parse("content://$authority/$tableName")
    private lateinit var db: RealEstateDatabase
    private var telephonyManager: TelephonyManager? = null
    private lateinit var context: Context
    private val apiKey = BuildConfig.GoogleSecAPIKEY
    private val address = "4 Avenue Jean Jaurès, 46100 Figeac France"

    // DATA SET FOR TEST
    private val estateId: Long = 2001
    private val estateId2: Long = 10000

    //Root test
    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RealEstateDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        context = InstrumentationRegistry.getInstrumentation().context
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
        telephonyManager = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    // Check ContentProvider when no item inserted
    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor: Cursor? = mContentResolver!!.query(
            ContentUris.withAppendedId(uriProperty, estateId2), null, null, null, null
        ).also {
            assertThat(it, notNullValue())
        }
        assertEquals(0, cursor?.count)
        cursor?.close()
    }

    // Get property and test contentProvider return
    @Test
    fun insertAndGetItem() {
        mContentResolver?.insert(uriProperty, createEstate())
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(uriProperty, estateId),
            null,
            null,
            null,
            null
        )
        assertNotNull(cursor)
        assertEquals(1, cursor?.count)
        assertEquals(true, cursor?.moveToFirst())
        assertEquals("Manor", cursor?.getString(cursor.getColumnIndexOrThrow("type")))

        //for clean database
       db.query(SimpleSQLiteQuery("DELETE FROM property WHERE id = $estateId"))
    }

    // Create property to test
    private fun createEstate(): ContentValues {
        val values = ContentValues()
        values.put("id", estateId)
        values.put("type", "Manor")
        values.put("price", 500000.0)
        values.put("surface", 550)
        values.put("roomNumber", 12)
        values.put("bathroomNumber", 4)
        values.put("bedroomNumber", 6)
        values.put("park", false)
        values.put("shops", false)
        values.put("school", false)
        values.put("status", true)
        values.put("dateOfEntry", "16-03-2020")
        values.put("dateOfSale", "")
        values.put("realtor", "JUL")
        return values
    }

    // Check if internet is available
    @Test
    fun checkIfInternetIsAvailableTest() {
        assertNotNull(Utils.isInternetAvailable(context))

        if(Utils.isInternetAvailable(context)){
            assertTrue(Utils.isInternetAvailable(context))
        }else{
            assertFalse(Utils.isInternetAvailable(context))
        }
    }

    //
    @Test
    fun checkIf_InternetIsAvailable() {
        val geocodeObservable = RealEstateStream().streamFetchGeocodeInfo(address, apiKey)
        val testObserver = TestObserver<GeocodeInfo>()
        geocodeObservable.subscribeWith(testObserver)
            .assertNoErrors()
            .assertNoTimeout()
            .awaitTerminalEvent()
        val geocodeInfo = testObserver.values()[0]
        if (geocodeInfo.status == "OK") {
            assertEquals(true, Utils.isInternetAvailable(context))
        } else {
            assertEquals(false, Utils.isInternetAvailable(context))
        }

    }

    // Check property values
    @Test
    fun checkValueBeforeStorePropertyTest() {
        val address = Address(
            0, "75 PARK PLACE 8TH FLOOR   ", "",
            2, "NEW YORK", "NY 10007", "United States",
            40.7808, -73.9772, ""
        )
        val propertyToSend = Property(
            0, "Manor", "cute house", 200000.0, 300, 9,
            shops = false,
            trainStation = false,
            park = false,
            airport = false,
            subway = false,
            school = false,
            status = true,
            dateOfEntry = "30-08-2020",
            dateOfSale = "",
            realtor = "JUl",
            numOfBath = 2,
            numOfBed = 4,
            address = Address(
                0, "75 PARK PLACE 8TH FLOOR   ", "",
                2, "NEW YORK", "NY 10007", "United States",
                40.7808, -73.9772, ""
            ),
            pictures = emptyList()
        )

        val checkClass = CreateEstateUtils()
        val propertyToReturn: Property
        propertyToReturn = checkClass.checkValueBeforeStoreProperty(
            InstrumentationRegistry.getInstrumentation().context, "Manor", 300,
            9, 4, 2, address, 200000.0, "Julien",
            "16-03-2020", "", true, propertyToSend, "NEW YORK", "NY 10007", "United States"
        )
        Assert.assertEquals(propertyToSend, propertyToReturn)
    }

    // Http test
    @Test
    fun streamGeocodeInfoTest() {
        val geocodeObservable = RealEstateStream().streamFetchGeocodeInfo(address, apiKey)
        val testObserver = TestObserver<GeocodeInfo>()
        geocodeObservable.subscribeWith(testObserver)
            .assertNoErrors()
            .assertNoTimeout()
            .awaitTerminalEvent()
        //val geocodeInfo = testObserver.values()[0]
        val geocodeInfo = testObserver.values().get(0)

        assertEquals("OK", geocodeInfo.status)
    }
}
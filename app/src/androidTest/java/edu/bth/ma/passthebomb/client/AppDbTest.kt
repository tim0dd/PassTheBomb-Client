package edu.bth.ma.passthebomb.client

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Ignore
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class AppDbTest {
/*

  //  private lateinit var challengeSetDao: ChallengeSetDao

  //  private lateinit var db: ChallengeSetDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockDatabaseVm : DatabaseVm
    *//* @Before
     fun setup() {
         val context = ApplicationProvider.getApplicationContext<Context>()
         db = Room.inMemoryDatabaseBuilder(
             context, ChallengeSetDatabase::class.java
         ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
         challengeSetDao = db.challengeSetDao()
     }*//*

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
      *//*  db = Room.inMemoryDatabaseBuilder(
            context, ChallengeSetDatabase::class.java
        ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
        challengeSetDao = db.challengeSetDao()*//*
        mockDatabaseVm = mockkClass(DatabaseVm::class)

    }

 *//*   @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }*//*

    @Test
    @Throws(Exception::class)
    fun getUser() {
        val challenge1 = Challenge("First Challenge", "First challenge text", 100)
        val challenge2 = Challenge("First Challenge", "First challenge text", 100)
        val challengeList = listOf(challenge1, challenge2)
        val challengeSet =
            ChallengeSet(null, 0, "Animals", Date(0), Date(0), Date(0), 1337, challengeList)
        mockDatabaseVm.addChallengeSet(challengeSet)
        runBlocking {
             delay(2000)
         }

        val byId = mockDatabaseVm.getChallengeSet(0)
        assertEquals(byId, challengeSet)
    }*/
}



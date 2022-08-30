# drive_series_tempate
Tutorial Android Room Database and Reactive Programming

Key Features
1.	Using the navigation graph
2.	Using SQLite for data storage and SQL for data retrieval
3.	Using class Flow for observation of list changes
4.	Using MVVM pattern for separation of data from UI
5.	Using dependency injection
Steps in the tutorial
1.	Project setup
2.	Models and data access
3.	Record viewing
4.	Implement business logic
Elevator Pitch
This introduces the currently most important topics in building an Android application. When completed this is a valuable template for starting many applications because most of the boiler plate code in integrated. Edit to need and continue developing. No deep dive into topics, search Bold Italicized for more information on that topic

Source code
https://github.com/dotrothschild/drive_series_tempate 
The app requires APK and target version 33 to run the android tests. Details at “Update build.gradle (module) compile and target SDK”

Project Description
These describe what the software must do and are the foundation for the test cases. The business requirement is a database of places. The application shall filter based on criteria and return a subset of those places.
User story 1:
•	Given: Player starts app
•	When: System gets and displays all places  
•	Then: Player views scrollable list of all places.
User story 2:
•	Given: Player selects place
•	When: System gets filter criteria
•	Then player views filtered list
Project Setup
Create a new project
1.	Start Android studio and from the main menu select File->New->New Project
2.	Select Phone and Tablet Templates, scroll project screens to bottom and select Fragment + View Model, click next
3.	Name project and select Minimum SDK, see New Project recommendations
Name: Drive
Package: com.<your URL>.drive 
Save location: …/<MyTemplates>/Drive
Language Kotlin
Minimum SDK: API 28
Update build.gradle (module) compile and target SDK
Verify or change android {compileSdk 33…. And defaultConfig{… targetSdk 33
This is required for the android unit tests to compile.
android {
    compileSdk 33

    defaultConfig {
        applicationId "com.dotrothschild.drive"
        minSdk 28
        targetSdk 33

Add Logging
Logging functionality is built in, but Jake Wharton Timber is better and easier to use
1.	Add the dependency Gradle Scripts/gradle.build (module)
implementation 'com.jakewharton.timber:timber:5.0.1'
2.	Need to synch the build files, but first update any newer versions. These are highlighted, hover over the highlight and click the recommended change. Upper right link, click Synch Now. 
3.	Create the application file. It is boiler plate code. On the app/java/com.<URL>.drive folder right click New->Kotlin Class Name=DriveApp, type Class
4.	Copy this code.
package com.<Your URL name appears here>.drive

import android.app.Application 
import timber.log.Timber

class DriveApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        Timber.i("Start App file on Create")
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "***DriveApp***: %s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        }
    }
}
5.	One of the better features of Timber, is optionally, add or modify the custom string on the return String.format(..).  I use ***DriveApp*** to filter for my logging messages.
6.	Modify the manifest, app/manifests, add this line just below <application 
android:name="com.dotrothschild.drive.DriveApp"
7.	Add debug statements in class MainActivity
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("Start onCreate")
    …

    Timber.d("End onCreate")
}
8.	Run the app, the virtual device API needs to be at or higher then the one selected when the app was created.
9.	To see the debug statements, click Logcat tab at the bottom of Android Studio. If nothing is displayed, stop the app, clean project (Menu: Build/Clean Project, then Menu: Build/ Rebuild) Run again
Logging setup is complete. Use Timber.d() to insert logging
Create Navigation
Add the following to Gradle Scripts / build.gradle (Module)
1.	Add viewBinding below android{… kotlinOptions {}
buildFeatures {
    viewBinding true
}
2.	In dependencies add 
implementation 'androidx.navigation:navigation-fragment-ktx:2.5.1' // need this for activity view models even w/o nav_graph
implementation 'androidx.navigation:navigation-ui-ktx:2.5.1' // need for nav_graph
3.	Add plugin to build.gradle (Module) in plugin
id 'androidx.navigation.safeargs.kotlin' // added for navigation
4.	Add plugin to build.gradle (Project, not Module) in plugin then Sync Now (upper right link)
id 'androidx.navigation.safeargs' version '2.5.1' apply false
5.	Create Nav Graph. Skip making the directory, it is automatically created when creating the navigation file. Right click res->New-> Android Resource File
•	File name= nav_graph
•	Resource type = change to Navigation, directory name is created, navigation, Ok
•	On displayed nav_graph.xml, click to add destination and select the fragment, fragment_main. Edit the properties displayed on the right
a.	Id = navigation_<fragmentName> ex: navigation_main
6.	Modify the res/layout activity_main.xml to use the nav_graph res/layout/activity_main. View split screen to edit the text, adding a container view, here fragments are displayed. MainFragment text now appears in the design 
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity" >

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph"/>
</FrameLayout>
7.	Modify main activity
•	Add the var navController
•	Add first line in onCreate Timber.d(“Starting…”)
•	Add the val binding in onCreate(
•	Add the val navHostFragment in onCreate(
•	Add Import android.navigation.ui.setupActionBarWithNavController
package com.dotrothschild.drive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dotrothschild.drive.databinding.ActivityMainBinding
import androidx.navigation.ui.setupActionBarWithNavController
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Start onCreate")
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        Timber.d("End onCreate")
    }
}
The app is now using navigation, nav_graph, for navigation and data passing between fragments.
Models and Data Access
Gradle
Add the following to Gradle Scripts / build.gradle (Module)
1.	In dependencies add 
implementation 'androidx.room:room-ktx:2.4.3'
kapt 'androidx.room:room-compiler:2.4.3'
implementation 'com.google.code.gson:gson:2.9.0'
2.	Add plugin to build.gradle (Module) in plugin then Sync Now (upper right link)
id 'kotlin-kapt'
id 'kotlin-parcelize'
Models
1.	Create new package database, under drive/ right click on drive, New->Package name=database. Under database create new package models. 
2.	Under drive/database/models create data class Place. Add each field in the database
3.	Class Place
@Entity(
    // TODO: WANT AN INDEX ON x,y so need to fix mock data
    tableName = "Place" // , indices = [Index(value = ["x", "y"], unique = true)]
    // risk that no 2 places same, not currently verified in db
)
@Parcelize
data class Place(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "state") val state: String,
    @NonNull @ColumnInfo(name = "country") val country: String,
    @NonNull @ColumnInfo(name = "x") val x: Double,
    @NonNull @ColumnInfo(name = "y") val y: Double,
    @ColumnInfo(name = "place_number") val place_number: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "region") val region: String?,
    @ColumnInfo(name = "keywords") val keywords: String?
) : Parcelable
4.	Create class Feedback under package models
@Entity(
    tableName = "Feedback"//, indices = [Index(value = ["placeId", "userName"], unique = true)]
    // risk that no 2 comments on same place by same user, not currently verified in db
)
@Parcelize
data class Feedback(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "place_id") val place_id: Int,
    @NonNull @ColumnInfo(name = "user_name") val user_name: String,
    @NonNull @ColumnInfo(name = "comment") val comment: String
) : Parcelable
Dao, Data access object
1.	At the package database.models create a new Kotlin class DriveDao
2.	Edit the package path, remove .models. Hover over line, select option more actions… and select move file to.. It is now under database, but same level as models.
3.	Copy code as below, it is an interface. Import kotlinx.coroutines.flow.Flow
@Dao
interface DriveDao {
    @Query("SELECT * FROM place ORDER BY title ASC")
    fun getAllPlaces(): Flow<List<Place>>
    @Query("SELECT * FROM feedback ORDER BY user_name ASC")
    fun getAllFeedback(): Flow<List<Feedback>>
}
Application Database
This defines a database and specifies data tables that will be used. Version is incremented as new tables/columns are added/removed/changed on device, without deleting the installed app. It is mostly boilerplate code.
1.	Create new Kotlin file at drive/database name it AppDatabase
2.	Copy code as below
@Database(entities = [Place::class, Feedback::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun driveDao(): DriveDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "drive_database")
                    .createFromAsset("database/drive.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}

3.	In the class DriveApp, add the var database before onCreate()  
class DriveApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
Sample Data
Use the provided drive.db or see Appendix A for creating sample data and a ready to ship 
SQLite database
1.	Right click app root, new directory, name=assets, and select src/main/assets for path
2.	Right click assets folder, new directory name=database
3.	Put the drive.db under the assets/database directory. Note in code above AppDatabase: .createFromAsset(“database/drive.db”)
View Model and Flow
Create new fragment to display a single record in a list. The records are viewed in a list. A record has a layout and code to display the record in the layout. 
1.	Right click on ui.main,  new->Fragment->Blank Fragment name=ListPlaceAdapter xml=list_place_item
2.	Each file is replaced with code below
List Place Item View
1.	Open file res/layout/list_place_item.xml
2.	Change view (upper rightfrom Design to split, and copy this code.
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="?selectableItemBackground"
    android:padding="16dp">

    <TextView
        android:id="@+id/textViewPlaceTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.AppCompat.Headline"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        tools:text="Title here" />

    <TextView
        android:id="@+id/textViewRDescription"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintLeft_toLeftOf="@+id/textViewPlaceTitle"
        app:layout_constraintTop_toBottomOf="@+id/textViewPlaceTitle"
        app:layout_constraintVertical_bias="0.0"
        tools:text="Description here" />

    <TextView
        android:id="@+id/textViewPlaceSubTitle1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toBottomOf="@+id/textViewRDescription"
        app:layout_constraintLeft_toLeftOf="@+id/textViewPlaceTitle"
        tools:text="Sub title 1" />
    <TextView
        android:id="@+id/textViewPlaceSubTitle2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toBottomOf="@+id/textViewPlaceSubTitle1"
        app:layout_constraintLeft_toLeftOf="@+id/textViewPlaceTitle"
        tools:text="Sub title 2" />
    <TextView
        android:id="@+id/textViewPlaceSubTitle3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toBottomOf="@+id/textViewPlaceSubTitle2"
        app:layout_constraintLeft_toLeftOf="@+id/textViewPlaceTitle"
        tools:text="Sub title 3" />
    <TextView
        android:id="@+id/textViewPlaceSubTitle4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.AppCompat.Subhead"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toBottomOf="@+id/textViewPlaceSubTitle3"
        app:layout_constraintLeft_toLeftOf="@+id/textViewPlaceTitle"
        tools:text="Sub title 4" />
</androidx.constraintlayout.widget.ConstraintLayout>
3.	The design appears like this
List Place Adapter
1.	Open file ui/main/ListPlaceAdapter.kt
package com.dotrothschild.drive.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.drive.database.models.Place
import com.dotrothschild.drive.databinding.ListPlaceItemBinding


class ListPlaceAdapter (
    private val onItemClicked: (Place) -> Unit
) : ListAdapter<Place, ListPlaceAdapter.ListPlaceViewHolder>(DiffCallback)
{
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaceViewHolder {
        val viewHolder = ListPlaceViewHolder(
            ListPlaceItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ListPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListPlaceViewHolder(
        private var binding: ListPlaceItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(place: Place) {
            binding.textViewPlaceTitle.text = place.title
            binding.textViewRDescription.text = place.description
            binding.textViewPlaceSubTitle1.text = place.address + ", " + place.city + ", " + place.region +", " + place.state + ", " + place.country
            binding.textViewPlaceSubTitle2.text = place.keywords
            binding.textViewPlaceSubTitle3.text = place.place_number
            binding.textViewPlaceSubTitle4.text = place.x.toString() + ", " + place.y.toString()
        }
    }
}
Model View, View Model
This separates the data from UI. The only way to pass a parameter to the view model on creation is using the factory. The factory is boiler plate code. Common initialization parameters are Dao and AppContext, but not the fragment.
Main View Model Changes
1.	Open file ui/main/MainViewModel
2.	Main fragment will call this method for data
fun allPlaces(): Flow<List<Place>> = driveDao.getAllPlaces()
3.	Need a factory because passing a parameter to the view model. This is all the code. import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotrothschild.drive.database.DriveDao
import com.dotrothschild.drive.database.models.Place
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val driveDao: DriveDao): ViewModel() {
    fun allPlaces(): Flow<List<Place>> = driveDao.getAllPlaces()
}
class MainViewModelFactory(
    private val driveDao: DriveDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(driveDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
Fragment_main.xml
1.	Open file res/layout/fragment_main.xml
<?xml version="1.0" encoding="utf-8"?>

<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.main.MainFragment"
    android:orientation="vertical">

    <TextView
        android:id="@+id/no_places_to_show"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Placeholder 1"
        android:textSize="16sp"
        android:gravity="center_horizontal"
        android:padding="8dp"
        app:layout_constraintWidth_percent="0.5"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toStartOf="parent"/>
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:layout_constraintTop_toBottomOf="@id/no_places_to_show"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
2.	Design appears like this 

Main Fragment Changes
1.	Open file ui/main/MainFragment
package com.dotrothschild.drive.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope 
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.drive.DriveApp
import com.dotrothschild.drive.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private  val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as DriveApp).database.driveDao()
        )
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // the onClick event added
        val listPlaceAdapter = ListPlaceAdapter {
        }

        recyclerView.adapter = listPlaceAdapter
        lifecycle.coroutineScope.launch{
            mainViewModel.allPlaces().collect {
                listPlaceAdapter.submitList(it)
                if (it.isEmpty()) {
                    _binding!!.noPlacesToShow.text  = "No places to display."
                } else {
                    _binding!!.noPlacesToShow.text  = "Total places: ${listPlaceAdapter.itemCount}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
2.	Run app it should appear like this 
Implementing User Story 2
On select of a place the user wants to see all feedback associated with that place.
High level steps
1.	Create classes
2.	Create test cases, they fail because there is no code in the class
3.	Write the code to pass the tests
Required classes
New package, feedback
1.	list_feedback_item.xml (use blank fragment template)
2.	ListFeedbackAdapter.kt (use blank fragment template)
3.	FeedbackViewModel.kt (use fragment + viewmodel template)
4.	FeedbackFragment.kt (use fragment + viewmodel template)
5.	MainFragment.kt (modify for onClick event)
6.	Navigation/nav_graph (modify)
New package
Right click ui.main new package: Replace .main. with .feedback
Add the following fragments to package feedback
1.	New -> Fragment (blank) Fragment Name= ListFeedbackAdapter Layout Name= list_feedback_item
2.	New -> Fragment -> Gallery, scroll down to Fragment (with ViewModel) Fragment Name= FeedbackFragment, remaining are automatically updated.
Update Files
Update nav_graph replace fragment with fragments and action. The action is onClick show the feedback fragment. Code needs to be added to the MainFragment.kt to navigate.
<fragment
    android:id="@+id/navigation_main"
    android:name="com.dotrothschild.drive.ui.main.MainFragment"
    android:label="fragment_main"
    tools:layout="@layout/fragment_main">
    <action
        android:id="@+id/action_navigation_main_to_navigation_feedback"
        app:destination="@id/navigation_feedback" />
</fragment>
<fragment
    android:id="@+id/navigation_feedback"
    android:name="com.dotrothschild.drive.ui.feedback.FeedbackFragment"
    android:label="fragment_feedback"
    tools:layout="@layout/fragment_feedback">
    <argument
        android:name="place_id"
        app:argType="integer" />
</fragment>

Unit Test Navigation
Before adding the code write the test of the expected result
1.	Open Class MainFragment, right click -> Generate… -> Test these settings
a.	Testing library Junit4 (NOT 5)
b.	Class name: (default) MainFragmentTest
c.	Destination package: (default) OK
d.	Select directory structure, (default) should be …/app/src/androidTest/… It is GUI, so must be in the androidTest directory. Pure Kotlin should be in the test directory.
2.	Add dependency in build.gradle (module) and sync
androidTestImplementation 'androidx.navigation:navigation-testing:2.5.1'
//below launch fragment in container
debugImplementation 'androidx.fragment:fragment-testing:1.6.0-alpha02' 
3.	MainFragmentTest
a.	Manually add import <your URL>.drive.R
b.	Add this test code and run
4.	Test fails because there is no code to navigate to the new fragment at com.dotrothschild.drive.databinding.ListPlaceItemBinding.inflate(ListPlaceItemBinding.java:67)
Modify MainFragment to Navigate
In onViewCreated() add
recyclerView.layoutManager = LinearLayoutManager(requireContext())

// the onClick event added
val listPlaceAdapter = ListPlaceAdapter {
    val action = MainFragmentDirections
        .actionNavigationMainToNavigationFeedback(
            placeId = it.id
        )
    view.findNavController().navigate(action)
}

Feedback
Modify list_feeback_item.xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="?selectableItemBackground"
    android:padding="16dp">

    <TextView
        android:id="@+id/textViewFeedbackTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.AppCompat.Headline"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        tools:text="Title here" />

    <TextView
        android:id="@+id/textViewRDescription"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintLeft_toLeftOf="@+id/textViewFeedbackTitle"
        app:layout_constraintTop_toBottomOf="@+id/textViewFeedbackTitle"
        app:layout_constraintVertical_bias="0.0"
        tools:text="Description here" />

    <TextView
        android:id="@+id/textViewFeedbackSubTitle1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintHorizontal_bias="0"
        app:layout_constraintTop_toBottomOf="@+id/textViewRDescription"
        app:layout_constraintLeft_toLeftOf="@+id/textViewFeedbackTitle"
        tools:text="Sub title 1" />
</androidx.constraintlayout.widget.ConstraintLayout>
Modify ListFeedbackAdapter.kt
package com.dotrothschild.drive.ui.feedback

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView 
import com.dotrothschild.drive.database.models.Feedback
import com.dotrothschild.drive.databinding.ListFeebackItemBinding

class ListFeedbackAdapter(
    private val onItemClicked: (Feedback) -> Unit
) : ListAdapter<Feedback, ListFeedbackAdapter.ListFeedbackViewHolder>(DiffCallback)
{
    companion object {
    private val DiffCallback = object : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(
            oldItem: Feedback,
            newItem: Feedback
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Feedback,
            newItem: Feedback
        ): Boolean {
            return oldItem == newItem
        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFeedbackViewHolder {
        val viewHolder = ListFeedbackViewHolder(
            ListFeebackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ListFeedbackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListFeedbackViewHolder(
        private var binding: ListFeebackItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(feedback: Feedback) {
            binding.textViewFeedbackTitle.text = feedback.user_name
            binding.textViewRDescription.text = feedback.comment
            binding.textViewFeedbackSubTitle1.text = feedback.place_id.toString()

        }
    }
}

Modify fragment_feedback
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.feedback.FeedbackFragment"
    android:orientation="vertical">
    <TextView
        android:id="@+id/no_feedback_records_to_show"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:gravity="center_horizontal"
        android:padding="8dp"
        app:layout_constraintWidth_percent="0.5"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:layout_constraintTop_toBottomOf="@id/no_feedback_records_to_show"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>

Modify FeedbackViewModel
class FeedbackViewModel(private val driveDao: DriveDao): ViewModel() {
    // fun allFeedback(): Flow<List<Feedback>> = driveDao.getAllFeedback() // This returns every record, works not used used
    fun feedbackByPlace(placeId: Int): Flow<List<Feedback>> = driveDao.getFeedbackByPlace(placeId) // this works, returns place id not name
    class FeedbackViewModelFactory(
        private val driveDao: DriveDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedbackViewModel(driveDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

Modify FeedbackFragment
class FeedbackFragment : Fragment() {

    private var placeId: Int = 0 // cannot use late init on primitive.

    private  val feedbackViewModel: FeedbackViewModel by activityViewModels {
        FeedbackViewModel.FeedbackViewModelFactory(
            (activity?.application as DriveApp).database.driveDao()
        )
    }
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeId = it.getInt("place_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listFeedbackAdapter = ListFeedbackAdapter {
        }
        recyclerView.adapter = listFeedbackAdapter
        lifecycle.coroutineScope.launch{
            feedbackViewModel.feedbackByPlace(placeId).collect {
                listFeedbackAdapter.submitList(it)
                if (it.isEmpty()) {
                    _binding!!.noFeedbackRecordsToShow.text  = "No feedback records on this place."
                } else {
                    _binding!!.noFeedbackRecordsToShow.text  = "Total feedback: ${listFeedbackAdapter.itemCount}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
Modify MainActivity
For the back arrow to work add this function
override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp() || super.onSupportNavigateUp()
}
 
Appendix A: Create SQLite Database

![image](https://user-images.githubusercontent.com/31523304/187360351-02aac958-d4f4-455a-9c9a-cbdc115ce37d.png)

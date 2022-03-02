package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity()
{

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // State controller, default: select
    private var lemonadeState = "select"
    // Amount of squeezes needed to change from squeeze to drink, default: -1
    private var lemonSize = -1
    // Amount of squeezes given, default: -1
    private var squeezeCount = -1

    //Creation of lemonTree obj, which controls the lemonSize variable
    private var lemonTree = LemonTree()
    //Image view for all the images
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handles UI states in code
        if (savedInstanceState != null)
        {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        //References the imageView
        lemonImage = findViewById(R.id.imageView)

        //Sets the default view up
        setViewElements()

        //Sets the onClickListener on the image and calls the click method
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }

        //Sets the longClickListener and shows a snackbar type of message
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
        }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle)
    {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage()
    {
        //When statement that handles the click function depending on the state
        when(lemonadeState)
        {
            "select" -> {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
                setViewElements()
            }
            "squeeze" -> {
                squeezeCount++
                lemonSize--
                if(lemonSize == 0)
                {
                    lemonadeState = DRINK
                    lemonSize = -1
                    squeezeCount = -1
                    setViewElements()
                }
            }
            "drink" -> {
                lemonadeState = RESTART
                setViewElements()
            }
            "restart" -> {
                lemonadeState = SELECT
                setViewElements()
            }
        }
    }

    /**
     * Sets up the view according to the state
     */
    private fun setViewElements()
    {
        //References the text view
        val textAction: TextView = findViewById(R.id.text_action)

        //Handles the image and text settings per state change
        when(lemonadeState)
        {
            "select" -> {
                textAction.text = getString(R.string.lemon_select)
                lemonImage?.setImageResource(R.drawable.lemon_tree)
            }
            "squeeze" -> {
                textAction.text = getString(R.string.lemon_squeeze)
                lemonImage?.setImageResource(R.drawable.lemon_squeeze)
            }
            "drink" -> {
                textAction.text = getString(R.string.lemon_drink)
                lemonImage?.setImageResource(R.drawable.lemon_drink)
            }
            "restart" -> {
                textAction.text = getString(R.string.lemon_empty_glass)
                lemonImage?.setImageResource(R.drawable.lemon_restart)
            }
        }


    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean
    {
        if (lemonadeState != SQUEEZE)
        {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree
{
    fun pick(): Int
    {
        return (2..6).random()
    }
}

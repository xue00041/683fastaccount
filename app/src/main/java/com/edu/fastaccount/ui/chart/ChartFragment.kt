package com.edu.fastaccount.ui.chart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.edu.fastaccount.R
import com.edu.fastaccount.data.DBHelper
import com.edu.fastaccount.data.UserManager
import com.edu.fastaccount.utils.TimeDateUtils
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

class ChartFragment : Fragment() {
    var mDbHelper: DBHelper? = null
    private val TAG = "jcy-ChartFragment"
    private var selectTime: Long = -1
    private var incomeEntries: ArrayList<PieEntry> = ArrayList<PieEntry>()
    private var expendEntries: ArrayList<PieEntry> = ArrayList<PieEntry>()
   private lateinit var income_chart: PieChart;
    private lateinit var expend_chart: PieChart;
    private lateinit var tv_expend: TextView;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_chart, container, false)
        income_chart=root.findViewById<PieChart>(R.id.income_chart)
        tv_expend=root.findViewById(R.id.tv_expend)
        expend_chart=root.findViewById<PieChart>(R.id.expend_chart)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDbHelper = DBHelper(requireContext())
        income_chart.setUsePercentValues(false)
        income_chart.getDescription().setEnabled(false)
        income_chart.setExtraOffsets(5f, 10f, 5f, 5f)
        expend_chart.setUsePercentValues(false)
        expend_chart.getDescription().setEnabled(false)
        expend_chart.setExtraOffsets(5f, 10f, 5f, 5f)
    }


    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun updateList() {
        var selectis = mDbHelper!!.selectis(UserManager.instance.user!!.id)
        Collections.reverse(selectis)
        if (null != selectis && selectis!!.size > 0) {
            // "Catering", "Sports", "Entertainment", "Daily"
            var incomeFood: Double = 0.0; // Income from catering
            var incomeSport: Double = 0.0; // Income from sports
            var incomeRecreation: Double = 0.0; // Income from entertainment
            var incomeDaily: Double = 0.0; // Income from daily expenses
            var expendFood: Double = 0.0; // Expenditure on catering
            var expendSport: Double = 0.0; // Expenditure on sports
            var expendRecreation: Double = 0.0; // Expenditure on entertainment
            var expendDaily: Double = 0.0; // Expenditure on daily expenses
            for (selecti in selectis!!) {
                val createTime: String = selecti.createTime!!
                val date: Date =
                    TimeDateUtils.string2Date(createTime, TimeDateUtils.FORMAT_TYPE_3)!!
                val itemItem: Long =
                    getZeroClockTimestamp(date.time)

                Log.d(
                    TAG,
                    "onClick:  selecti " + selecti.type + "  selecti " + selecti
                )
                if (selectTime == -1L || itemItem == selectTime) {

                    if (selecti.type == 1) {
                        // Expenditure
                        if (selecti.costType.equals("Catering")) {
                            incomeFood = incomeFood + selecti.money!!
                        } else if (selecti.costType.equals("Sports")) {
                            incomeSport = incomeSport + selecti.money!!
                        } else if (selecti.costType.equals("Entertainment")) {
                            incomeRecreation = incomeRecreation + selecti.money!!
                        } else if (selecti.costType.equals("Daily")) {
                            incomeDaily = incomeDaily + selecti.money!!
                        }
                    } else {
                        // Income
                        if (selecti.costType.equals("Catering")) {
                            expendFood = expendFood + selecti.money!!
                        } else if (selecti.costType.equals("Sports")) {
                            expendSport = expendSport + selecti.money!!
                        } else if (selecti.costType.equals("Entertainment")) {
                            expendRecreation = expendRecreation + selecti.money!!
                        } else if (selecti.costType.equals("Daily")) {
                            expendDaily = expendDaily + selecti.money!!
                        }
                    }
                }
            }
            incomeEntries.clear()
            var allIncome =
                incomeFood.toFloat() + incomeSport.toFloat() + incomeRecreation.toFloat() + incomeDaily.toFloat()
            tv_expend.setText("Total Income  : ${allIncome}")
            if (allIncome > 0f) {
                if (incomeFood > 0.0) {
                    incomeEntries.add(PieEntry(incomeFood.toFloat(), "Catering"))
                }
                if (incomeSport > 0.0) {
                    incomeEntries.add(PieEntry(incomeSport.toFloat(), "Sports"))
                }
                if (incomeRecreation > 0.0) {
                    incomeEntries.add(PieEntry(incomeRecreation.toFloat(), "Entertainment"))
                }
                if (incomeDaily > 0.0) {
                    incomeEntries.add(PieEntry(incomeDaily.toFloat(), "Daily"))
                }

                income_chart.visibility = View.VISIBLE
                val colors = ArrayList<Int>()

                colors.add(Color.parseColor("#FFEF5350"))
                colors.add(Color.parseColor("#FF26A69A"))
                colors.add(Color.parseColor("#FF7E57C2"))
                colors.add(Color.parseColor("#FFFFCA28"))
                updateChart(incomeEntries, income_chart, colors)
            } else {
                income_chart.visibility = View.GONE
            }

            expendEntries.clear()
            var allExpend =
                expendFood.toFloat() + expendSport.toFloat() + expendRecreation.toFloat() + expendDaily.toFloat()
            tv_expend.setText("Total Expenditure  : ${allExpend}")
            if (allExpend > 0f) {
                if (expendFood > 0.0) {
                    expendEntries.add(PieEntry(expendFood.toFloat(), "Catering"))
                }
                if (expendSport > 0.0) {
                    expendEntries.add(PieEntry(expendSport.toFloat(), "Sports"))
                }
                if (expendRecreation > 0.0) {
                    expendEntries.add(PieEntry(expendRecreation.toFloat(), "Entertainment"))
                }
                if (expendDaily > 0.0) {
                    expendEntries.add(PieEntry(expendDaily.toFloat(), "Daily"))
                }

                val colors = ArrayList<Int>()
                colors.add(Color.parseColor("#FFEC407A"))
                colors.add(Color.parseColor("#FF9CCC65"))
                colors.add(Color.parseColor("#FF42A5F5"))
                colors.add(Color.parseColor("#FFFF7043"))
                expend_chart.visibility = View.VISIBLE
                updateChart(expendEntries, expend_chart, colors)
            } else {
                expend_chart.visibility = View.GONE
            }
        }
    }

    private fun updateChart(entries: ArrayList<PieEntry>, chart: PieChart, colors: ArrayList<Int>) {
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // add a lot of colors

        dataSet.setDrawValues(true)
        dataSet.setColors(colors)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        chart.setData(data)
        // undo all highlights
        chart.highlightValues(null)
        chart.isDrawHoleEnabled = false
        chart.setDrawCenterText(false)
        chart.invalidate()
    }

    fun getZeroClockTimestamp(time: Long): Long {
        return (time - (time + TimeZone.getDefault()
            .rawOffset) % (24 * 60 * 60 * 1000)) / 1000
    }
}
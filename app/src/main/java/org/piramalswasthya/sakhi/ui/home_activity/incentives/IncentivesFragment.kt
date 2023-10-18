package org.piramalswasthya.sakhi.ui.home_activity.incentives

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.adapters.IncentiveListAdapter
import org.piramalswasthya.sakhi.databinding.FragmentIncentivesBinding
import org.piramalswasthya.sakhi.helpers.Konstants
import org.piramalswasthya.sakhi.helpers.getDateString
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class IncentivesFragment : Fragment() {

    private var _binding: FragmentIncentivesBinding? = null
    private val binding: FragmentIncentivesBinding
        get() = _binding!!


    private val viewModel: IncentivesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncentivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = IncentiveListAdapter()
        val divider = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.rvIncentive.addItemDecoration(divider)
        binding.rvIncentive.adapter = adapter
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setStart(Konstants.defaultTimeStamp)
                        .setEnd(System.currentTimeMillis())
                        .build()
                )
                .build()
        binding.ibEditCalendar.setOnClickListener {
            dateRangePicker.show(childFragmentManager, "CALENDAR")
        }
        dateRangePicker.addOnPositiveButtonClickListener {
            viewModel.setRange(it.first, it.second)
        }
        lifecycleScope.launch{
            viewModel.from.collect{
                binding.tvFrom.text = getDateString(it)
            }
        }
        lifecycleScope.launch{
            viewModel.to.collect{
                binding.tvTo.text = getDateString(it)
            }
        }

        lifecycleScope.launch {
            viewModel.incentiveList.collect {
                adapter.submitList(it)
                val activityList = it.map { it.activity }
                val pending = activityList.filter { !it.isPaid }.sumOf { it.rate }
                val processed = activityList.filter { it.isPaid }.sumOf { it.rate }
                binding.tvTotalPending.text = getString(R.string.incentive_pending, pending)
                binding.tvTotalProcessed.text = getString(R.string.incentive_processed, processed)
                binding.tvLastupdated.text =
                    getString(R.string.incentive_last_updated, viewModel.lastUpdated)
            }
        }

        binding.tvTotalPending.setOnClickListener {
            downloadPdf()
        }
    }


    override fun onStart() {
        super.onStart()
        activity?.let {
            (it as HomeActivity).updateActionBar(
                R.drawable.ic__incentive,
                getString(R.string.incentive_fragment_title)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun downloadPdf() {


        val document = PdfDocument()

        val pageWidth = 300
        val pageHeight = 400
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 6f

        val x = 10
        var y = 50
        val columnWidth = 100
        val rowHeight = 15

        canvas.drawText("ASHA Incentive Master Claim Form", (x + columnWidth).toFloat(), y.toFloat(), paint)
        y+= 5

        canvas.drawLine(x.toFloat(), y.toFloat(), (pageWidth - 2*x).toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("To", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("SDM&HO or i/c Block PHC", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("_______________________", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("Sub: Submission of ASHA incentive claim for the period from ${binding.tvFrom.text} to ${binding.tvTo.text}", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("Sir/Madam,   With reference to the subject cited above, I have to the honour to submit the ASHA incentive claims ", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("for the period from ${binding.tvFrom.text} to ${binding.tvTo.text} as per statement mentioned below.", x.toFloat(), y.toFloat(), paint)
        y += rowHeight


        // Draw the table data
//        canvas.drawText("Data 1", x.toFloat(), y.toFloat(), paint)
//        canvas.drawText("Data 2", (x + columnWidth).toFloat(), y.toFloat(), paint)

        // Finish the page
        document.finishPage(page)

        // Start a new page
        val pageInfo1 = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create()
        val page1 = document.startPage(pageInfo1)

        val canvas1 = page1.canvas
        y = 50 // Reset y position

        // Draw more data on the second page
        canvas1.drawText("Data 3", x.toFloat(), y.toFloat(), paint)
        canvas1.drawText("Data 4", (x + columnWidth).toFloat(), y.toFloat(), paint)

        // Finish the second page
        document.finishPage(page1)

        // You can continue with more pages if needed

        // Save the PDF file
        val fileName = "incentives.pdf"
        val directory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, fileName)

        try {
            document.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        document.close()

    }


}
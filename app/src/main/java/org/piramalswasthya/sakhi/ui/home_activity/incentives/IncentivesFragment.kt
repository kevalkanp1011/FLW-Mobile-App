package org.piramalswasthya.sakhi.ui.home_activity.incentives

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
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
import org.piramalswasthya.sakhi.model.IncentiveDomain
import org.piramalswasthya.sakhi.ui.home_activity.HomeActivity
import org.piramalswasthya.sakhi.utils.HelperUtil.drawMultilineText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

@AndroidEntryPoint
class IncentivesFragment : Fragment() {

    private var _binding: FragmentIncentivesBinding? = null
    private val binding: FragmentIncentivesBinding
        get() = _binding!!


    private var incentiveDomainList : List<IncentiveDomain> = mutableListOf()

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
                incentiveDomainList = it
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

        val pageNumber = 1

        incentiveDomainList.forEach {

        }
        val pageWidth = 300
        val pageHeight = 400
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 6f

        val textPaint = TextPaint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 6f

        val x = 10
        var y = 50
        val columnWidth = 100
        val rowHeight = 15

        var maxPages = incentiveDomainList.size / 20 + 2

        var currentPage = 1

        canvas.drawText(resources.getString(R.string.asha_incentive_master_claim_form), (x + columnWidth).toFloat(), y.toFloat(), paint)
        y+= 5

        canvas.drawLine(x.toFloat(), y.toFloat(), (pageWidth - 2*x).toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText(resources.getString(R.string.to), x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText(resources.getString(R.string.sdm_ho_or_i_c_block_phc), x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText("_______________________", x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText(resources.getString(R.string.sub_submission_of_asha_incentive_claim_for_the_period_from)
                + binding.tvFrom.text + resources.getString(R.string.to_small)
                + binding.tvTo.text,
            x.toFloat(), y.toFloat(), paint)
        y += rowHeight

        canvas.drawText(resources.getString(R.string.sir_madam), x.toFloat(), y.toFloat(), paint)
//        y += rowHeight

        canvas.drawMultilineText(
            text = resources.getString(R.string.with_reference_to_)
                    + binding.tvFrom.text + resources.getString(R.string.to_small)
                    + binding.tvTo.text + resources.getString(R.string.as_per_statement),
            textPaint = textPaint,
            width = pageWidth - 2*x,
            x = x.toFloat(),
            y = y.toFloat(),
            0)
        y += rowHeight

        var currentGroup = ""
        incentiveDomainList.forEach {
            if (y > pageHeight) {
                document.finishPage(page)
                currentPage++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create()
                page = document.startPage(pageInfo)
                y = rowHeight
            } else {
                if (currentGroup.contentEquals(it.activity.group)) {
                    it.record.name?.let { it1 -> canvas.drawText(it1, x.toFloat(), y.toFloat(), paint) }
                    y += rowHeight
                } else {
                    canvas.drawText(it.activity.group, x.toFloat(), y.toFloat(), paint)
                    y += rowHeight
                    it.record.name?.let { it1 -> canvas.drawText(it1, x.toFloat(), y.toFloat(), paint) }
                    y += rowHeight
                }
                currentGroup = it.activity.group
            }
        }
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
        val fileName = "incentives_" + (Calendar.getInstance().timeInMillis) + ".pdf"
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
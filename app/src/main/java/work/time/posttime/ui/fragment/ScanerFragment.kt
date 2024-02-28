package work.time.worktim.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import work.time.worktim.R
import work.time.worktim.databinding.FragmentScanerBinding


class ScanerFragment : Fragment(), ZBarScannerView.ResultHandler {

    private lateinit var vbView: ZBarScannerView
    private lateinit var binding: FragmentScanerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanerBinding.inflate(inflater, container, false)
        vbView = ZBarScannerView(requireContext())
        return vbView
    }

    override fun onResume() {
        super.onResume()
        vbView.setAutoFocus(true)
        vbView.setResultHandler(this)
        vbView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        vbView.stopCamera()
    }

    override fun handleResult(result: Result?) {

        result?.let {
            val dataToSend = it.contents.toString()
            findNavController().navigate(R.id.homeFragment, bundleOf(RESULT to dataToSend))
        }

        // Очистка результатов сканирования (необязательно)
        vbView.resumeCameraPreview(this)
    }

    companion object {
        const val RESULT = "resultqwe"
    }
}
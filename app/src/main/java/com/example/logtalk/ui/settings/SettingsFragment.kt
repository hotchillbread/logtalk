package com.example.logtalk.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

// Hilt를 사용하기 때문에 @AndroidEntryPoint 어노테이션이 필요합니다.
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ComposeView를 반환하여 Compose 코드를 호스팅합니다.
        return ComposeView(requireContext()).apply {
            // Fragment의 라이프사이클에 맞게 Composition을 설정합니다.
            setViewCompositionStrategy(androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                // 프로젝트의 테마를 적용합니다.
                // import com.example.logtalk.ui.theme.LogtalkTheme // 프로젝트 테마 경로 가정
                // LogtalkTheme {

                // 💡 SettingsScreen Composable을 호출합니다.
                // SettingsScreen 내부에서 hiltViewModel()을 사용하므로,
                // 별도의 ViewModel 주입 없이 Hilt가 알아서 처리합니다.
                SettingsScreen()
                // }
            }
        }
    }
}
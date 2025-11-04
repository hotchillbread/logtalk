package com.example.logtalk.ui.chat

// ChatContract.kt

/**
 * View와 Presenter 간의 통신 규약(Contract)을 정의합니다.
 * 이를 통해 View는 Presenter에, Presenter는 View의 인터페이스에 의존하게 되어 DIP를 준수합니다.
 */
interface ChatContract {

    /**
     * View 인터페이스: View (Activity/Fragment)가 Presenter에게 노출하는 기능입니다.
     */
    interface View {
        // UI 업데이트를 위한 함수 (Presenter가 호출)
        fun displayMessage(message: Message, isUser: Boolean)
        fun updateMessageList(messages: List<Message>)
        fun clearInputText()
        fun showLoadingIndicator(show: Boolean)
        fun showToast(message: String)

        // 음성 입력 관련 (Whisper API 연동)
        fun setInputTextFromVoice(text: String)
        fun startListeningForVoice()
        fun stopListeningForVoice()
    }

    /**
     * Presenter 인터페이스: View가 Presenter에게 요청하는 기능입니다.
     */
    interface Presenter {
        fun attachView(view: View)
        fun detachView()

        fun loadInitialMessages()
        fun sendMessage(text: String)
        fun startVoiceInput()
        fun stopVoiceInput(audioData: ByteArray?) // 실제로는 STT API 호출

        // 유사 상담 추천 (RAG 검색 기능)
        fun suggestSimilarConsults()
    }
}

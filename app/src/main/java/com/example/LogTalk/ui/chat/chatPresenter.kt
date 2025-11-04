package com.example.logtalk.ui.chat

// ChatPresenter.kt (비즈니스 로직 및 중재자: Presenter 역할)

import com.example.logtalk.domain.usecase.SendMessageUseCase // 가정된 Use Case
import com.example.logtalk.SearchUseCase    // 가정된 Use Case

// Presenter 인터페이스를 구현합니다.
class ChatPresenter(
    private val sendMessageUseCase: SendMessageUseCase,
    private val ragSearchUseCase: RagSearchUseCase
) : ChatContract.Presenter {

    // View에 대한 약한 참조 (메모리 누수 방지 목적)
    private var view: ChatContract.View? = null

    override fun attachView(view: ChatContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadInitialMessages() {
        // 1. 초기 메시지 로드 (DB/RAG에서)
        // val messages = GetInitialMessagesUseCase.execute()
        // view?.updateMessageList(messages)
        view?.updateMessageList(
            listOf(
                Message("오늘의 기분은 어떠신가요?\n저에게 알려주세요", 1L, "AI"),
                Message("흑흑 안드로이드 스튜디오만 돌리면 컴퓨터가 죽으려고 해 ㅠ", 2L, "User")
            )
        )
    }

    override fun sendMessage(text: String) {
        if (text.isBlank()) return

        // 1. 사용자 메시지 즉시 표시
        val userMessage = Message(text, System.currentTimeMillis(), "User")
        view?.displayMessage(userMessage, true)
        view?.clearInputText()
        view?.showLoadingIndicator(true) // AI 응답 대기 로딩 시작

        // 2. 비즈니스 로직 (Use Case 호출)
        // CoroutineScope 사용하여 백그라운드에서 실행해야 함
        // launch {
        //     val aiResponse = sendMessageUseCase.execute(userMessage) // GPT-4o API 호출
        //     view?.showLoadingIndicator(false)
        //     view?.displayMessage(aiResponse, false)
        // }
    }

    override fun startVoiceInput() {
        view?.startListeningForVoice()
        // 실제 음성 인식 시작 (OS API 호출)
    }

    override fun stopVoiceInput(audioData: ByteArray) {
        view?.stopListeningForVoice()
        // 1. STT 로직 시작
        // val transcript = WhisperApi.process(audioData)
        // 2. STT 결과를 View의 입력창에 반영
        // view?.setInputTextFromVoice(transcript)
    }

    override fun suggestSimilarConsults() {
        // 1. RAG 검색 Use Case 호출 (현재 대화 내용을 기반으로 유사 상담 검색)
        // val similarList = ragSearchUseCase.execute(currentSessionContext)
        // 2. 결과를 View에 알림 (예: 다이얼로그 표시)
        // view?.showToast("유사한 과거 상담 ${similarList.size}건 발견")
    }
}

// ----------------------------------------------------
// Dummy/가정 클래스 (실제 구현 필요)
// ----------------------------------------------------
interface SendMessageUseCase { fun execute(message: Message): Message }
interface RagSearchUseCase { fun execute(context: String): List<Message> }
package com.example.logtalk.ui.chat

// ChatView.kt (채팅 화면: View 역할)

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.logtalk.R

// MVP 패턴의 View 역할을 수행하며 ChatContract.View 인터페이스를 구현합니다.
class ChatView : AppCompatActivity(), ChatContract.View {

    // Presenter 객체 (Presenter 인터페이스에 의존 - DIP)
    private lateinit var presenter: ChatContract.Presenter

    //메세지 입력
    private lateinit var messageInput: EditText
    // 전송 버튼
    private lateinit var sendButton: ImageButton
    //음성 버튼
    private lateinit var voiceInputButton: ImageButton
    //메세지 컴포넌트
    private lateinit var messageRecyclerView: RecyclerView
    //채팅 컴포넌트
    private lateinit var chatAdapter: MessageAdapter // 가정된 Adapter 클래스

    private var isListening = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) // 가정된 레이아웃 파일

        // Presenter 초기화 및 주입 (실제 앱에서는 DI 라이브러리 사용 권장)
        // 여기서는 예시로 구현체를 바로 생성합니다.
        presenter = ChatPresenter(
            // Use Case와 Repository는 DI를 통해 주입되었다고 가정
            sendMessageUseCase = GetInstance.sendMessageUseCase,
            ragSearchUseCase = GetInstance.ragSearchUseCase
        )
        presenter.attachView(this)

        initViews()
        initListeners()

        presenter.loadInitialMessages()
    }

    private fun initViews() {
        // ID는 레이아웃에 맞게 가정한 이름입니다.
        messageInput = findViewById(R.id.edit_text_message_input)
        sendButton = findViewById(R.id.button_send)
        voiceInputButton = findViewById(R.id.button_voice_input)
        messageRecyclerView = findViewById(R.id.recycler_view_messages)

        chatAdapter = MessageAdapter()
        messageRecyclerView.adapter = chatAdapter
        // 레이아웃 매니저 설정 (예: LinearLayoutManager)
    }

    private fun initListeners() {
        // 전송 버튼 클릭
        sendButton.setOnClickListener {
            val text = messageInput.text.toString().trim()
            if (text.isNotEmpty()) {
                presenter.sendMessage(text)
            }
        }

        // 음성 입력 버튼 클릭 시
        voiceInputButton.setOnClickListener {
            // 현재 상태에 따라 STT 시작/중지 요청
            if (isListening) {
                presenter.stopVoiceInput(null)
            } else {
                presenter.startVoiceInput()
            }
        }
    }

    // --- ChatContract.View 구현 ---

    override fun displayMessage(message: Message, isUser: Boolean) {
        // chatAdapter.addMessage(message)
        // messageRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun updateMessageList(messages: List<Message>) {
        // chatAdapter.submitList(messages)
    }

    override fun clearInputText() {
        messageInput.text.clear()
    }

    override fun showLoadingIndicator(show: Boolean) {
        // 로딩 스피너 표시/숨기기 UI 로직
    }

    override fun showToast(message: String) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Whisper API로부터 변환된 텍스트를 입력창에 자동 채움
    override fun setInputTextFromVoice(text: String) {
        messageInput.setText(text)
    }

    // 음성 입력 시작 (UI 상태 변경)
    override fun startListeningForVoice() {
        isListening = true
        voiceInputButton.setImageResource(R.drawable.ic_stop) // 아이콘 변경
        // OS/라이브러리 Speech Recognizer 호출 로직은 여기에 포함
    }

    // 음성 입력 중지 (UI 상태 변경)
    override fun stopListeningForVoice() {
        isListening = false
        voiceInputButton.setImageResource(R.drawable.ic_mic) // 아이콘 변경
    }

    // --- 생명주기 관리 ---

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView() // 메모리 누수 방지
    }
}

// ----------------------------------------------------
// Dummy/가정 클래스 (실제 구현 필요)
// -----------------------------------------------------

data class Message(val content: String, val timestamp: Long, val senderId: String)

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

class GetInstance {
    companion object {
        val sendMessageUseCase: Any = Any()
        val ragSearchUseCase: Any = Any()
    }
}

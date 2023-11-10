import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


private const val API_KEY = "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"

// BerufswahlViewModel.kt
class BerufswahlViewModel : ViewModel() {

    private val _recommendation = MutableStateFlow<String?>(null)
    val recommendation: StateFlow<String?> = _recommendation

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val mChatGPTService =
        ChatGPTService(API_KEY, "https://api.openai.com/v1/chat/completions")

    private val prompt = """
        Antworte als Berufsberater. 
        Ich bin auf der Suche nach einem passenden Beruf für mich. 
        Könntest du mir ein paar Vorschläge machen basierend auf folgenden Aussagen: 
    """.trimIndent()


    fun askChatGPT(interessen: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = mChatGPTService.request(prompt+interessen)
                _recommendation.value = response
            } catch (e: Exception) {
                _error.value = "Error happened: $e"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        mChatGPTService.close()
    }
}


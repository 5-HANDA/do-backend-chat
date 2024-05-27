// package hobbiedo.chat.application;
//
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Map;
// import java.util.Set;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
//
// import hobbiedo.chat.dto.request.ChatSendDTO;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// /*
//  * WebSocket Handler 작성
//  * 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
//  * 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
//  * TextWebSocketHandler를 상속받아 핸들러 작성
//  * 클라이언트로 받은 메세지를 log로 출력하고 클라이언트로 환영 메세지를 보내줌
//  * */
// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class WebSocketChatHandler extends TextWebSocketHandler {
// 	private final ObjectMapper mapper;
//
// 	// 현재 연결된 세션들
// 	private final Set<WebSocketSession> sessions = new HashSet<>();
//
// 	// chatRoomId: {session1, session2}
// 	private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
//
// 	// 소켓 연결 시
// 	@Override
// 	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
// 		// TODO Auto-generated method stub
// 		log.info("{} 연결됨", session.getId());
// 		sessions.add(session);
// 	}
//
// 	// 소켓 통신 시 메세지 전송 시
// 	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws
// 		Exception {
// 		String payload = message.getPayload();
// 		log.info("payload {}", payload);
//
// 		// 페이로드 -> chatMessageDto로 변환
// 		ChatSendDTO chatSendDTO = mapper.readValue(payload, ChatSendDTO.class);
// 		log.info("session {}", chatSendDTO.toString());
//
// 		Long chatRoomId = chatSendDTO.getCrewId();
// 		// 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
// 		if (!chatRoomSessionMap.containsKey(chatRoomId)) {
// 			chatRoomSessionMap.put(chatRoomId, new HashSet<>());
// 		}
// 		Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);
// 		// 소모임 가입 시 필요
// 		// if (chatSendDTO.getMessageType().equals(ChatSendDTO.MessageType.ENTER)) {
// 		// 	// sessions 에 넘어온 session 을 담고,
// 		// 	chatRoomSession.add(session);
// 		// }
// 		if (chatRoomSession.size() >= 3) {
// 			// 지금 단체채팅에 있는 세션들이, 현재 돌아가고 있는 세션인지 확인하고 없으면 제거
// 			removeClosedSession(chatRoomSession);
// 		}
// 		sendMessageToChatRoom(chatSendDTO, chatRoomSession);
//
// 	}
//
// 	// 소켓 종료 시
// 	@Override
// 	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws
// 		Exception {
// 		// TODO Auto-generated method stub
// 		log.info("{} 연결 끊김", session.getId());
// 		sessions.remove(session);
// 	}
//
// 	// ====== 채팅 관련 메소드 ======
// 	private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
// 		chatRoomSession.removeIf(sess -> !sessions.contains(sess));
// 	}
//
// 	private void sendMessageToChatRoom(ChatSendDTO chatSendDTO,
// 		Set<WebSocketSession> chatRoomSession) {
// 		chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatSendDTO));
// 	}
//
// 	public <T> void sendMessage(WebSocketSession session, T message) {
// 		try {
// 			session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
// 		} catch (IOException e) {
// 			log.error(e.getMessage(), e);
// 		}
// 	}
// }

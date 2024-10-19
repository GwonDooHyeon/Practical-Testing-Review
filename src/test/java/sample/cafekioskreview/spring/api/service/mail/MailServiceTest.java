package sample.cafekioskreview.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekioskreview.spring.client.mail.MailSendClient;
import sample.cafekioskreview.spring.domain.history.mail.MailSendHistory;
import sample.cafekioskreview.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    
    @Mock
    private MailSendClient mailSendClient;

//    @Spy
//    private MailSendClient mailSendClient;
    
    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;
    
    @InjectMocks
    private MailService mailService;
    
    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        // Mock
//        when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
            .willReturn(true);
        
        // Spy
//        doReturn(true)
//            .when(mailSendClient).sendMail(anyString(), anyString(), anyString(), anyString());
        
        // when
        boolean result = mailService.sendMail("", "", "", "");
        
        // then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }
    
}

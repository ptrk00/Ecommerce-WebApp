package com.example.EcommerceApp.security.service;

import com.example.EcommerceApp.security.exceptions.UserFieldNotUniqueException;
import com.example.EcommerceApp.security.model.RegistrationDetails;
import com.example.EcommerceApp.security.model.User;
import com.example.EcommerceApp.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void should_register_user_when_username_and_email_are_not_already_present_in_db() {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("username");
        registrationDetails.setPassword("passwd");
        registrationDetails.setEmail("email@email.pl");

        // email and username are unique in db

        when(userRepository.findByUsername(any(String.class))).thenReturn(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("ENCODED");

        User user = registrationDetails.toUser(passwordEncoder);

        // when

        registrationService.registerUser(registrationDetails);

        // then

        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void should_encode_user_password() {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("username");
        registrationDetails.setPassword("passwd");
        registrationDetails.setEmail("email@email.pl");

        // email and username are unique in db

        when(userRepository.findByUsername(any(String.class))).thenReturn(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("ENCODED");

        // first call to encode

        User user = registrationDetails.toUser(passwordEncoder);
        when(userRepository.save(user)).thenReturn(user);

        // when

        // second call to encode
        user = registrationService.registerUser(registrationDetails);

        // then

        assertThat(user.getPassword()).isEqualTo("ENCODED");
        verify(passwordEncoder,times(2)).encode(any(CharSequence.class));

    }

    @Test
    public void should_not_register_user_when_username_or_email_are_already_present_in_db() {

        // given

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setUsername("username");
        registrationDetails.setPassword("passwd");
        registrationDetails.setEmail("email@email.pl");

        User user = registrationDetails.toUser(passwordEncoder);


        // user with username 'username' already present in db

        when(userRepository.findByUsername("username")).thenReturn(user);

        // when + then

        assertThatThrownBy( () -> registrationService.registerUser(registrationDetails)
        ).isInstanceOf(UserFieldNotUniqueException.class).hasMessageContaining("username");

        verify(userRepository,never()).save(user);
    }

}
package tech.alianza.clients.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.alianza.clients.domain.User;
import org.springframework.stereotype.Service;
import tech.alianza.clients.repository.UserRepository;
import tech.alianza.clients.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public EmailExistResponse checkEmailExist(String email) {
        boolean emailExist = userRepository.selectExistsEmail(email);
        return EmailExistResponse.builder()
                .emailExist(emailExist)
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        String[] arrOfName = request.getName().trim().split(" ", 2);
        StringBuilder sb = new StringBuilder();
        if(arrOfName.length > 1) {
            sb.append(arrOfName[0].toLowerCase().charAt(0));
            sb.append(arrOfName[1].toLowerCase());
        } else {
            sb.append(arrOfName[0].toLowerCase());
        }
        String username = sb.toString();
        var user = User.builder()
                .name(request.getName())
                .username(username)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .locked(false)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

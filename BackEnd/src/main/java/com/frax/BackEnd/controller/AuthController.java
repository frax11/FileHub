    package com.frax.BackEnd.controller;

    import java.util.Map;

    import lombok.RequiredArgsConstructor;

    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.frax.BackEnd.dto.UserDTO;
    import com.frax.BackEnd.dto.UserLoginDTO;
    import com.frax.BackEnd.dto.UserRegistrationDTO;
    import com.frax.BackEnd.services.UserInfoService;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.validation.Valid;

    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;



    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/auth")
    public class AuthController {
        
        private final UserInfoService userService;
        private final AuthenticationManager authenticationManager;


        
        @PostMapping("/login")
        public ResponseEntity<?> logIn(@Valid @RequestBody UserLoginDTO loginDTO) {
            try{
                Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));  
                // 2. Salva l'autenticazione nel contesto (Spring lo farà anche automaticamente)
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 3. A questo punto, il cookie JSESSIONID è già stato creato da Spring
                //Angular lo riceverà automaticamente nell'header Set-Cookie della risposta, e lo invierà nelle richieste successive.
                return ResponseEntity.ok("Login effettuato");
                    
            }catch(Exception e){
                return ResponseEntity.status(401).body("Autenticazione Fallita: " + e.getMessage());
            }
            // Autentica l'utente

        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDto) {
            try {
                UserDTO createdUser = userService.saveUser(userDto);
                return ResponseEntity.ok(createdUser);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Errore durante la registrazione: " + e.getMessage());
            }
            
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
            // Recupera l'utente autenticato
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null) {
                // Invalida la sessione e cancella il cookie
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            
            return ResponseEntity.ok("Logout effettuato");
        }

        @PostMapping("/me")
        public ResponseEntity<?> getMe(Authentication authentication) {
            if(authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Utente non autenticato");
            }
            return ResponseEntity.ok(authentication.getPrincipal());
        }

    }
    package com.frax.BackEnd.controller;

    import lombok.RequiredArgsConstructor;

    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
    import org.springframework.security.web.csrf.CsrfToken;
    import org.springframework.web.bind.annotation.*;

    import com.frax.BackEnd.dto.UserDTO;
    import com.frax.BackEnd.dto.UserLoginDTO;
    import com.frax.BackEnd.dto.UserRegistrationDTO;
    import com.frax.BackEnd.services.UserService;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.validation.Valid;


    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/user")
    public class UserController {

        private final AuthenticationManager authenticationManager;
        private final UserService userService;



        @PostMapping("/login")
        public ResponseEntity<?> logIn(@Valid @RequestBody UserLoginDTO loginDTO) {
            try{
                // Autenticazione utente
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
                );
                // Salva il contesto di sicurezza
                return ResponseEntity.ok("Login effettuato");
            }catch(Exception e){
                return ResponseEntity.status(401).body("email o password non trovati");
            }

        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDTO) {
            try {
                UserDTO createdUser = userService.saveUser(userDTO);
                return ResponseEntity.ok(createdUser);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Errore nella registrazione, utente già registrato." );
            }

        }

        @PostMapping("/logout")
        public ResponseEntity<?> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return ResponseEntity.ok("Logout effettuato");
        }

        @PostMapping("/delete")
        public ResponseEntity<?> deleteUser(Authentication authentication,HttpServletRequest request, HttpServletResponse response) {
            try {
                userService.deleteUser(authentication.getName());
            }catch (Exception e){
                return ResponseEntity.status(401).body(e);
            }

            return  ResponseEntity.ok("User deleted");
        }

        @GetMapping("/me")
        public ResponseEntity<?> getMe(Authentication authentication) {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Utente non autenticato");
            }
            String email = authentication.getName();
            return ResponseEntity.ok(authentication.getPrincipal());
        }

        @GetMapping("/csrf")
        public CsrfToken getCsrf(CsrfToken token) {
            return token;
        }


    }
    package com.frax.BackEnd.controller;

    import jakarta.servlet.http.HttpSession;
    import lombok.RequiredArgsConstructor;

    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContext;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
    import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
        
        private final UserInfoService userInfoService;
        private final AuthenticationManager authenticationManager;


        
        @PostMapping("/login")
        public ResponseEntity<?> logIn(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletRequest request,HttpServletResponse response) {
            try{
                Authentication authentication   = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
                securityContextRepository.saveContext(securityContext, request, response);
                return ResponseEntity.ok("Login effettuato");
                    
            }catch(Exception e){
                return ResponseEntity.status(401).body("Autenticazione Fallita: " + e.getMessage());
            }
            // Autentica l'utente

        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDto, HttpServletRequest request,HttpServletResponse response) {
            try {
                UserDTO createdUser = userInfoService.saveUser(userDto);
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



    }
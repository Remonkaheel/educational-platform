package com.global.hr.controllers;
import com.global.hr.models.DTO.ProfileResponse;
import com.global.hr.models.User;
import com.global.hr.repositories.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Endpoint to fetch profile data
    @GetMapping("/profile")
    public ProfileResponse getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // Get the email of the currently logged-in user
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token="fuck";
		return new ProfileResponse(user,token);
    }
}

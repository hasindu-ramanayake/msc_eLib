package com.elib.user.config;

import com.elib.user.entity.Address;
import com.elib.user.entity.NotificationPreference;
import com.elib.user.entity.Role;
import com.elib.user.entity.User;
import com.elib.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Seeds the user_db with mock users on application startup if the database is
 * empty.
 * Creates one ADMIN, one STAFF, and several CUSTOMER accounts for testing
 * purposes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("DataSeeder: Database already has {} users. Skipping seed.", userRepository.count());
            return;
        }

        log.info("DataSeeder: Seeding user database with mock data...");

        List<User> users = List.of(
                // Admin user
                User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@elib.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .phoneNumber("+353871234567")
                        .notificationPreferences(Set.of(NotificationPreference.EMAIL, NotificationPreference.SMS))
                        .address(Address.builder()
                                .addressLine1("1 Admin Street")
                                .addressLine2("Dublin 1")
                                .eircode("D01AB12")
                                .county("Dublin")
                                .build())
                        .build(),

                // Staff user
                User.builder()
                        .firstName("Staff")
                        .lastName("Member")
                        .email("staff@elib.com")
                        .password(passwordEncoder.encode("Staff@123"))
                        .role(Role.STAFF)
                        .phoneNumber("+353872345678")
                        .notificationPreferences(Set.of(NotificationPreference.EMAIL))
                        .address(Address.builder()
                                .addressLine1("2 Library Lane")
                                .addressLine2("Cork City")
                                .eircode("T12CD34")
                                .county("Cork")
                                .build())
                        .build(),

                // Customer 1
                User.builder()
                        .firstName("Alice")
                        .lastName("Murphy")
                        .email("alice.murphy@example.com")
                        .password(passwordEncoder.encode("Customer@123"))
                        .role(Role.CUSTOMER)
                        .phoneNumber("+353873456789")
                        .notificationPreferences(Set.of(NotificationPreference.EMAIL))
                        .address(Address.builder()
                                .addressLine1("10 River Road")
                                .addressLine2("Galway City")
                                .eircode("H91EF56")
                                .county("Galway")
                                .build())
                        .build(),

                // Customer 2
                User.builder()
                        .firstName("Bob")
                        .lastName("O'Brien")
                        .email("bob.obrien@example.com")
                        .password(passwordEncoder.encode("Customer@123"))
                        .role(Role.CUSTOMER)
                        .phoneNumber("+353874567890")
                        .notificationPreferences(Set.of(NotificationPreference.SMS))
                        .address(Address.builder()
                                .addressLine1("5 Oak Avenue")
                                .addressLine2("Limerick City")
                                .eircode("V94GH78")
                                .county("Limerick")
                                .build())
                        .build(),

                // Customer 3
                User.builder()
                        .firstName("Clara")
                        .lastName("Walsh")
                        .email("clara.walsh@example.com")
                        .password(passwordEncoder.encode("Customer@123"))
                        .role(Role.CUSTOMER)
                        .phoneNumber("+353875678901")
                        .notificationPreferences(Set.of(NotificationPreference.EMAIL, NotificationPreference.SMS))
                        .address(Address.builder()
                                .addressLine1("22 Maple Close")
                                .addressLine2("Waterford City")
                                .eircode("X91IJ90")
                                .county("Waterford")
                                .build())
                        .build());

        List<User> savedUsers = userRepository.saveAll(users);
        log.info("DataSeeder: Successfully seeded {} users.", savedUsers.size());
        log.info("DataSeeder: Credentials and ID (UUID) summary:");

        for (User user : savedUsers) {
            log.info("  [{}] {} ({}) -> UUID: {}",
                    user.getRole(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail(),
                    user.getId());
        }
    }
}

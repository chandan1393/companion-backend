package com.xelvo.companion.config;

import com.xelvo.companion.availability.entity.AvailabilitySlot;
import com.xelvo.companion.availability.repository.AvailabilityRepository;
import com.xelvo.companion.booking.entity.*;
import com.xelvo.companion.booking.repository.BookingRepository;
import com.xelvo.companion.companion.entity.*;
import com.xelvo.companion.companion.repository.CompanionRepository;
import com.xelvo.companion.experience.entity.Experience;
import com.xelvo.companion.experience.repository.ExperienceRepository;
import com.xelvo.companion.favorite.entity.Favorite;
import com.xelvo.companion.favorite.repository.FavoriteRepository;
import com.xelvo.companion.message.entity.Message;
import com.xelvo.companion.message.repository.MessageRepository;
import com.xelvo.companion.payment.entity.Payment;
import com.xelvo.companion.payment.repository.PaymentRepository;
import com.xelvo.companion.review.entity.Review;
import com.xelvo.companion.review.repository.ReviewRepository;
import com.xelvo.companion.user.entity.*;
import com.xelvo.companion.user.entity.Role;
import com.xelvo.companion.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            CompanionRepository companionRepository,
            ExperienceRepository experienceRepository,
            AvailabilityRepository availabilityRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            ReviewRepository reviewRepository,
            MessageRepository messageRepository,
            FavoriteRepository favoriteRepository,
            PasswordEncoder passwordEncoder,
            TransactionTemplate transactionTemplate) {

        return args -> transactionTemplate.executeWithoutResult(status -> {

            // =========================================================
            // USERS
            // =========================================================

            // Customers
            User customer = user(
                    userRepository,
                    passwordEncoder,
                    "Chandan Sharma",
                    "user@companion.demo",
                    "user123",
                    Role.USER,
                    "9876543210",
                    "Delhi"
            );

            User customer2 = user(
                    userRepository,
                    passwordEncoder,
                    "Rohan Mehta",
                    "rohan@companion.demo",
                    "user123",
                    Role.USER,
                    "9876543211",
                    "Delhi"
            );

            User customer3 = user(
                    userRepository,
                    passwordEncoder,
                    "Ananya Kapoor",
                    "ananya@companion.demo",
                    "user123",
                    Role.USER,
                    "9876543212",
                    "Gurugram"
            );

            // Companions
            User priyaUser = user(
                    userRepository,
                    passwordEncoder,
                    "Priya Singh",
                    "companion@companion.demo",
                    "companion123",
                    Role.COMPANION,
                    "9876500000",
                    "Delhi"
            );

            User arjunUser = user(
                    userRepository,
                    passwordEncoder,
                    "Arjun Malhotra",
                    "arjun@companion.demo",
                    "companion123",
                    Role.COMPANION,
                    "9876500001",
                    "Gurugram"
            );

            User simranUser = user(
                    userRepository,
                    passwordEncoder,
                    "Simran Kaur",
                    "simran@companion.demo",
                    "companion123",
                    Role.COMPANION,
                    "9876500002",
                    "Noida"
            );

            User kabirUser = user(
                    userRepository,
                    passwordEncoder,
                    "Kabir Verma",
                    "kabir@companion.demo",
                    "companion123",
                    Role.COMPANION,
                    "9876500003",
                    "Delhi"
            );

            // Admin
            user(
                    userRepository,
                    passwordEncoder,
                    "Platform Admin",
                    "admin@companion.demo",
                    "admin123",
                    Role.ADMIN,
                    "9876509999",
                    "Delhi"
            );

            // =========================================================
            // COMPANION PROFILES
            // =========================================================

            CompanionProfile priya = companion(
                    companionRepository,
                    priyaUser,
                    "Priya",
                    27,
                    "Delhi",
                    "Female",
                    "Coffee, photography, city walks and discovering hidden local places.",
                    new BigDecimal("600.00"),
                    List.of(
                            "Coffee",
                            "Travel",
                            "Food",
                            "Photography",
                            "Movies",
                            "Art",
                            "Music",
                            "City walks"
                    ),
                    "https://placehold.co/900x700/e6c2a4/173d73?text=Priya"
            );

            CompanionProfile arjun = companion(
                    companionRepository,
                    arjunUser,
                    "Arjun",
                    30,
                    "Gurugram",
                    "Male",
                    "Food explorer and weekend planner. I enjoy restaurants, live events and long conversations.",
                    new BigDecimal("750.00"),
                    List.of(
                            "Food",
                            "Events",
                            "Nightlife",
                            "Gaming",
                            "Travel",
                            "Cricket"
                    ),
                    "https://placehold.co/900x700/c7d9e8/173d73?text=Arjun"
            );

            CompanionProfile simran = companion(
                    companionRepository,
                    simranUser,
                    "Simran",
                    26,
                    "Noida",
                    "Female",
                    "Creative, outdoorsy and always looking for a new workshop, trail or art experience.",
                    new BigDecimal("700.00"),
                    List.of(
                            "Art",
                            "Fitness",
                            "Outdoor",
                            "Workshops",
                            "Learning",
                            "Photography"
                    ),
                    "https://placehold.co/900x700/e6c9d9/173d73?text=Simran"
            );

            CompanionProfile kabir = companion(
                    companionRepository,
                    kabirUser,
                    "Kabir",
                    29,
                    "Delhi",
                    "Male",
                    "Easy-going companion for city exploration, concerts, shopping and relaxed evenings.",
                    new BigDecimal("650.00"),
                    List.of(
                            "City walks",
                            "Concerts",
                            "Shopping",
                            "Movies",
                            "Coffee",
                            "Music"
                    ),
                    "https://placehold.co/900x700/d7c6aa/173d73?text=Kabir"
            );

            // =========================================================
            // EXPERIENCES
            // =========================================================

            Experience priyaCoffee = experience(
                    experienceRepository,
                    priya,
                    "Coffee & Conversation",
                    "Relaxed one-on-one conversation at a café.",
                    "600.00",
                    60,
                    "Coffee"
            );

            Experience priyaCity = experience(
                    experienceRepository,
                    priya,
                    "City Exploration",
                    "Explore local spots and hidden gems together.",
                    "1000.00",
                    120,
                    "Travel"
            );

            Experience arjunFood = experience(
                    experienceRepository,
                    arjun,
                    "Food Trail",
                    "Explore a curated food trail with local favourites.",
                    "1200.00",
                    120,
                    "Food"
            );

            Experience arjunEvents = experience(
                    experienceRepository,
                    arjun,
                    "Live Event Companion",
                    "Attend a concert, comedy show or live event together.",
                    "1500.00",
                    180,
                    "Events"
            );

            Experience simranArt = experience(
                    experienceRepository,
                    simran,
                    "Art & Workshop Hour",
                    "Visit a creative space and try a hands-on workshop.",
                    "900.00",
                    90,
                    "Art"
            );

            Experience simranTrail = experience(
                    experienceRepository,
                    simran,
                    "Outdoor Trail",
                    "A relaxed outdoor walk followed by coffee.",
                    "800.00",
                    120,
                    "Outdoor"
            );

            Experience kabirCity = experience(
                    experienceRepository,
                    kabir,
                    "Old Delhi Walk",
                    "Discover historic streets, markets and hidden food spots.",
                    "850.00",
                    120,
                    "City Exploration"
            );

            Experience kabirConcert = experience(
                    experienceRepository,
                    kabir,
                    "Concert Buddy",
                    "Enjoy a live music event with a friendly companion.",
                    "1100.00",
                    150,
                    "Concerts"
            );

            // =========================================================
            // AVAILABILITY
            // =========================================================

            seedAvailability(
                    availabilityRepository,
                    priya,
                    1
            );

            seedAvailability(
                    availabilityRepository,
                    arjun,
                    2
            );

            seedAvailability(
                    availabilityRepository,
                    simran,
                    3
            );

            seedAvailability(
                    availabilityRepository,
                    kabir,
                    4
            );

            // =========================================================
            // DATES
            // =========================================================

            LocalDate past = LocalDate.now().minusDays(5);
            LocalDate tomorrow = LocalDate.now().plusDays(1);

            // =========================================================
            // COMPLETED BOOKINGS
            // =========================================================

            Booking completedPriya = booking(
                    bookingRepository,
                    paymentRepository,
                    customer,
                    priya,
                    priyaCoffee,
                    past,
                    LocalTime.of(16, 0),
                    "The Brew Room, Connaught Place, Delhi",
                    "Great conversation and a lovely café.",
                    BookingStatus.COMPLETED
            );

            Booking completedArjun = booking(
                    bookingRepository,
                    paymentRepository,
                    customer2,
                    arjun,
                    arjunFood,
                    LocalDate.now().minusDays(8),
                    LocalTime.of(18, 0),
                    "Cyber Hub, Gurugram",
                    "Looking forward to trying new food.",
                    BookingStatus.COMPLETED
            );

            Booking completedSimran = booking(
                    bookingRepository,
                    paymentRepository,
                    customer3,
                    simran,
                    simranArt,
                    LocalDate.now().minusDays(3),
                    LocalTime.of(11, 0),
                    "Noida Art District",
                    "Workshop was the highlight of the week.",
                    BookingStatus.COMPLETED
            );

            // =========================================================
            // UPCOMING BOOKINGS
            // =========================================================

            booking(
                    bookingRepository,
                    paymentRepository,
                    customer,
                    priya,
                    priyaCity,
                    tomorrow,
                    LocalTime.of(10, 0),
                    "India Gate, Delhi",
                    "Let's explore the city.",
                    BookingStatus.CONFIRMED
            );

            booking(
                    bookingRepository,
                    paymentRepository,
                    customer2,
                    arjun,
                    arjunEvents,
                    tomorrow.plusDays(1),
                    LocalTime.of(17, 0),
                    "DLF Cyber Hub, Gurugram",
                    "Please meet near the main entrance.",
                    BookingStatus.PENDING
            );

            booking(
                    bookingRepository,
                    paymentRepository,
                    customer3,
                    simran,
                    simranTrail,
                    tomorrow.plusDays(3),
                    LocalTime.of(9, 0),
                    "Noida Sector 18",
                    "Morning trail.",
                    BookingStatus.CONFIRMED
            );

            booking(
                    bookingRepository,
                    paymentRepository,
                    customer,
                    kabir,
                    kabirCity,
                    tomorrow.plusDays(4),
                    LocalTime.of(15, 0),
                    "Chandni Chowk, Delhi",
                    "Interested in the food stops too.",
                    BookingStatus.PENDING
            );

            // =========================================================
            // REVIEWS
            // =========================================================

            review(
                    reviewRepository,
                    completedPriya,
                    5,
                    "Priya was friendly, punctual and made the meetup very comfortable."
            );

            review(
                    reviewRepository,
                    completedArjun,
                    4,
                    "Great food recommendations and easy conversation."
            );

            review(
                    reviewRepository,
                    completedSimran,
                    5,
                    "The workshop was fun and Simran made the whole experience easy."
            );

            // =========================================================
            // FAVORITES
            // =========================================================

            favorite(
                    favoriteRepository,
                    customer,
                    arjun
            );

            favorite(
                    favoriteRepository,
                    customer,
                    simran
            );

            favorite(
                    favoriteRepository,
                    customer2,
                    priya
            );

            favorite(
                    favoriteRepository,
                    customer3,
                    kabir
            );

            // =========================================================
            // MESSAGES
            // =========================================================

            message(
                    messageRepository,
                    customer,
                    priyaUser,
                    "Hi Priya, looking forward to our coffee meetup!"
            );

            message(
                    messageRepository,
                    priyaUser,
                    customer,
                    "Hi Chandan! See you tomorrow at 10 AM."
            );

            message(
                    messageRepository,
                    customer2,
                    arjunUser,
                    "Is the food trail suitable for vegetarian options?"
            );

            message(
                    messageRepository,
                    arjunUser,
                    customer2,
                    "Yes, I can include several vegetarian stops."
            );

            message(
                    messageRepository,
                    customer3,
                    simranUser,
                    "What should I bring for the outdoor trail?"
            );

            message(
                    messageRepository,
                    simranUser,
                    customer3,
                    "Comfortable shoes and water are enough."
            );
        });
    }

    // =============================================================
    // USER
    // =============================================================

    private User user(
            UserRepository repo,
            PasswordEncoder encoder,
            String name,
            String email,
            String password,
            Role role,
            String phone,
            String city) {

        User u = repo.findByEmailIgnoreCase(email)
                .orElseGet(() ->
                        new User(
                                name,
                                email,
                                encoder.encode(password),
                                role
                        )
                );

        u.updateProfile(
                name,
                phone,
                city
        );

        u.changeStatus(
                UserStatus.ACTIVE
        );

        return repo.save(u);
    }

    // =============================================================
    // COMPANION
    // =============================================================

    private CompanionProfile companion(
            CompanionRepository repo,
            User user,
            String name,
            int age,
            String city,
            String gender,
            String bio,
            BigDecimal rate,
            List<String> interests,
            String photo) {

        CompanionProfile p = repo.findByUserId(user.getId())
                .orElseGet(() ->
                        new CompanionProfile(
                                user,
                                name,
                                age,
                                city,
                                gender,
                                bio,
                                rate
                        )
                );

        p.updateProfile(
                name,
                age,
                city,
                gender,
                bio,
                null,
                rate
        );

        p.changeStatus(
                CompanionStatus.VERIFIED
        );

        p.updateInterests(
                interests
        );

        if (p.getPhotoUrls().isEmpty()) {
            p.addPhoto(photo);
        }

        return repo.save(p);
    }

    // =============================================================
    // EXPERIENCE
    // =============================================================

    private Experience experience(
            ExperienceRepository repo,
            CompanionProfile companion,
            String title,
            String description,
            String price,
            int duration,
            String category) {

        return repo.findByCompanionId(companion.getId())
                .stream()
                .filter(e ->
                        e.getTitle().equalsIgnoreCase(title)
                )
                .findFirst()
                .map(e -> {

                    e.update(
                            title,
                            description,
                            new BigDecimal(price),
                            duration,
                            category
                    );

                    e.setActive(true);

                    return repo.save(e);
                })
                .orElseGet(() ->
                        repo.save(
                                new Experience(
                                        companion,
                                        title,
                                        description,
                                        new BigDecimal(price),
                                        duration,
                                        category
                                )
                        )
                );
    }

    // =============================================================
    // AVAILABILITY
    // =============================================================

    private void seedAvailability(
            AvailabilityRepository repo,
            CompanionProfile companion,
            int offset) {

        for (int i = 1; i <= 7; i++) {

            LocalDate date =
                    LocalDate.now()
                            .plusDays(i + offset - 1L);

            slot(
                    repo,
                    companion,
                    date,
                    LocalTime.of(10, 0),
                    LocalTime.of(13, 0)
            );

            slot(
                    repo,
                    companion,
                    date,
                    LocalTime.of(15, 0),
                    LocalTime.of(19, 0)
            );
        }
    }

    private void slot(
            AvailabilityRepository repo,
            CompanionProfile companion,
            LocalDate date,
            LocalTime start,
            LocalTime end) {

        boolean exists =
                repo.findByCompanionIdAndAvailableDateBetweenOrderByAvailableDateAscStartTimeAsc(
                                companion.getId(),
                                date,
                                date
                        )
                        .stream()
                        .anyMatch(s ->
                                s.getStartTime().equals(start)
                                        && s.getEndTime().equals(end)
                        );

        if (!exists) {

            repo.save(
                    new AvailabilitySlot(
                            companion,
                            date,
                            start,
                            end
                    )
            );
        }
    }

    // =============================================================
    // BOOKING
    // =============================================================

    private Booking booking(
            BookingRepository bookingRepo,
            PaymentRepository paymentRepo,
            User customer,
            CompanionProfile companion,
            Experience experience,
            LocalDate date,
            LocalTime start,
            String location,
            String note,
            BookingStatus status) {

        Booking booking =
                bookingRepo
                        .findByCustomerIdOrderByBookingDateDescStartTimeDesc(
                                customer.getId()
                        )
                        .stream()
                        .filter(b ->
                                b.getCompanion().getId().equals(companion.getId())
                                        && b.getExperience().getId().equals(experience.getId())
                                        && b.getBookingDate().equals(date)
                                        && b.getStartTime().equals(start)
                        )
                        .findFirst()
                        .orElseGet(() ->
                                bookingRepo.save(
                                        new Booking(
                                                customer,
                                                companion,
                                                experience,
                                                date,
                                                start,
                                                start.plusMinutes(
                                                        experience.getDurationMinutes()
                                                ),
                                                experience.getPrice(),
                                                experience.getPrice()
                                                        .multiply(
                                                                new BigDecimal("0.10")
                                                        ),
                                                location,
                                                note
                                        )
                                )
                        );

        if (booking.getStatus() != status) {
            booking.changeStatus(status);
        }

        booking = bookingRepo.save(
                booking
        );

        // Update completed booking count
        if (status == BookingStatus.COMPLETED
                && companion.getCompletedBookings() < 1) {

            companion.incrementCompletedBookings();
        }

        // =========================================================
        // PAYMENT
        // =========================================================

        Booking finalBooking = booking;
        Payment payment =
                paymentRepo
                        .findByBookingId(booking.getId())
                        .orElseGet(() ->
                                paymentRepo.save(
                                        new Payment(finalBooking)
                                )
                        );

        if (status == BookingStatus.COMPLETED
                || status == BookingStatus.CONFIRMED) {

            payment.markSuccess(
                    "DEMO_ORDER_" + booking.getId(),
                    "DEMO_PAYMENT_" + booking.getId()
            );

            paymentRepo.save(
                    payment
            );
        }

        return booking;
    }

    // =============================================================
    // REVIEW
    // =============================================================

    private void review(
            ReviewRepository repo,
            Booking booking,
            int rating,
            String comment) {

        if (repo.existsByBookingId(booking.getId())) {
            return;
        }

        repo.save(
                new Review(
                        booking,
                        booking.getCustomer(),
                        booking.getCompanion(),
                        rating,
                        comment
                )
        );

        CompanionProfile companion =
                booking.getCompanion();

        int oldCount =
                companion.getReviewCount();

        BigDecimal total =
                companion
                        .getRating()
                        .multiply(
                                BigDecimal.valueOf(oldCount)
                        )
                        .add(
                                BigDecimal.valueOf(rating)
                        );

        BigDecimal newRating =
                total.divide(
                        BigDecimal.valueOf(oldCount + 1),
                        2,
                        java.math.RoundingMode.HALF_UP
                );

        companion.addReview(
                oldCount + 1,
                newRating
        );
    }

    // =============================================================
    // FAVORITE
    // =============================================================

    private void favorite(
            FavoriteRepository repo,
            User user,
            CompanionProfile companion) {

        if (!repo.existsByUserIdAndCompanionId(
                user.getId(),
                companion.getId())) {

            repo.save(
                    new Favorite(
                            user,
                            companion
                    )
            );
        }
    }

    // =============================================================
    // MESSAGE
    // =============================================================

    private void message(
            MessageRepository repo,
            User sender,
            User recipient,
            String content) {

        boolean exists =
                repo.findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByCreatedAtAsc(
                                sender.getId(),
                                recipient.getId(),
                                recipient.getId(),
                                sender.getId()
                        )
                        .stream()
                        .anyMatch(m ->
                                m.getContent().equals(content)
                        );

        if (!exists) {

            repo.save(
                    new Message(
                            sender,
                            recipient,
                            content
                    )
            );
        }
    }
}
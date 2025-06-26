//package roomescape.reservation.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//import org.junit.jupiter.params.provider.NullSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import roomescape.reservation.domain.Reservation;
//import roomescape.reservation.service.dto.ReservationRequestDto;
//import roomescape.reservation.service.dto.ReservationResponseDto;
//import roomescape.reservation.service.ReservationService;
//import roomescape.reservationTime.domain.ReservationTime;
//import roomescape.theme.domain.Theme;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.stream.Stream;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith({MockitoExtension.class})
//class ReservationApiControllerTest {
//    public static final String BASE_URL = "/reservations";
//
//    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//    ReservationTime reservationTime = ReservationTime.builder()
//            .startAt(LocalTime.of(10, 0))
//            .build();
//    Theme theme = Theme.builder()
//            .name("방이름")
//            .description("방설명")
//            .thumbnail("썸네일")
//            .build();
//    Reservation reservation = Reservation.builder()
//            .id(1L)
//            .name("이름")
//            .date(LocalDate.of(2025, 12, 31))
//            .theme(theme)
//            .reservationTime(reservationTime)
//            .build();
//    ReservationRequestDto reservationRequestDto = new ReservationRequestDto("이름", LocalDate.of(2025, 12, 31), 1L, 1L);
//    ReservationResponseDto reservationResponseDto = new ReservationResponseDto(reservation);
//
//
//    private MockMvc mockMvc;
//    @InjectMocks
//    private AdminReservationApiController reservationApiController;
//    @Mock
//    private ReservationService reservationService;
//
//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(reservationApiController).build();
//    }
//
//    @Nested
//    @DisplayName("[DELETE] 예약 삭제")
//    class DeleteReservation {
//        @Test
//        @DisplayName("204: 예약 삭제 성공")
//        void deleteReservation() throws Exception {
//            doNothing().when(reservationService).deleteById(any());
//
//            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isNoContent());
//        }
//    }
//
//    @Nested
//    @DisplayName("[GET] 예약 조회")
//    class GetReservation {
//        @Test
//        @DisplayName("200: 조회 성공")
//        void success() throws Exception {
//            when(reservationService.findById(any())).thenReturn(reservation);
//
//            mockMvc.perform(
//                            MockMvcRequestBuilders.get(BASE_URL + "/1"))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(reservationResponseDto.id()))
//                    .andExpect(jsonPath("$.name").value(reservationResponseDto.name()))
//                    .andExpect(jsonPath("$.date").value(reservationResponseDto.date().format(DateTimeFormatter.ofPattern(ReservationResponseDto.DATE_PATTERN))))
//                    .andExpect(jsonPath("$.themeName").value(reservationResponseDto.themeName()))
//                    .andExpect(jsonPath("$.time").value(reservationResponseDto.time().format(DateTimeFormatter.ofPattern(ReservationResponseDto.TIME_PATTERN))));
//
//        }
//    }
//
//    @Nested
//    @DisplayName("[POST] 예약 생성")
//    class Create {
//
//        public static Stream<Arguments> provideStringsForDateException() {
//            String name = "테스트";
//            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
//            return Stream.of(
//                    Arguments.of(name, "2024:06:21", time),
//                    Arguments.of(name, "2024", time)
//            );
//        }
//
//        public static Stream<Arguments> provideStringsForTimeException() {
//            String name = "테스트";
//            String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            return Stream.of(
//                    Arguments.of(name, date, "24:00"), // 24 넘는 숫자
//                    Arguments.of(name, date, "12-00"),
//                    Arguments.of(name, date, "1200")
//
//            );
//        }
//
//        @Test
//        @DisplayName("201: 생성에 성공 한다.")
//        void createReservation() throws Exception {
//
//            when(reservationService.save(reservationRequestDto)).thenReturn(reservation);
//
//            mockMvc.perform(
//                            MockMvcRequestBuilders.post(BASE_URL)
//                                    .content(objectMapper.writeValueAsString(reservationRequestDto))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").value(reservationResponseDto.id()))
//                    .andExpect(jsonPath("$.date").value(reservationResponseDto.date().format(DateTimeFormatter.ofPattern(ReservationResponseDto.DATE_PATTERN))))
//                    .andExpect(jsonPath("$.time").value(reservationResponseDto.time().format(DateTimeFormatter.ofPattern(ReservationResponseDto.TIME_PATTERN))));
//
//
//        }
//
//        @DisplayName("400: id 입력 예외")
//        @ParameterizedTest
//        @NullAndEmptySource
//        void nameException(String name) throws Exception {
//            mockMvc.perform(
//                            MockMvcRequestBuilders.post(BASE_URL)
//                                    .content("""
//                                            {
//                                                "id" : "%s",
//                                                "date" : "%s",
//                                                "time" : "%s"
//                                            }
//                                            """.formatted(name, reservation.getDate().toString(), reservation.getReservationTime().toString()))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isBadRequest());
//        }
//
//        @DisplayName("400: Date 입력 형식 예외")
//        @ParameterizedTest
//        @MethodSource("provideStringsForDateException")
//        @NullSource
//        void dateException(String date) throws Exception {
//            mockMvc.perform(
//                            MockMvcRequestBuilders.post(BASE_URL)
//                                    .content("""
//                                            {
//                                                "id" : "%s",
//                                                "date" : "%s",
//                                                "time" : "%s"
//                                            }
//                                            """.formatted(reservation.getMember(), date, reservationTime.getStartAt().toString()))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isBadRequest());
//        }
//
//        @DisplayName("400: Time 입력 형식 예외")
//        @ParameterizedTest
//        @MethodSource("provideStringsForTimeException")
//        void timeException(String time) throws Exception {
//            mockMvc.perform(
//                            MockMvcRequestBuilders.post(BASE_URL)
//                                    .content("""
//                                            {
//                                                "id" : "%s",
//                                                "date" : "%s",
//                                                "time" : "%s"
//                                            }
//                                            """.formatted(reservation.getMember(), reservation.getDate().toString(), time))
//                                    .contentType(MediaType.APPLICATION_JSON))
//                    .andDo(MockMvcResultHandlers.print())
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//}
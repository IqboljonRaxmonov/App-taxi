//package ai.ecma.server.controller;
//
//
//import ai.ecma.server.entity.User;
//import ai.ecma.server.payload.Result;
//import ai.ecma.server.payload.TariffDto;
//import ai.ecma.server.security.CurrentUser;
//import ai.ecma.server.service.TariffService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/tariff")
//public class TariffController {
//    @Autowired
//    TariffService tariffService;
//
//
//    @GetMapping
//    public List<TariffDto> getTariffListByDriverId(@CurrentUser User user) {
//        return tariffService.getTariffListByDriverId(user);
//    }
//
//    @PreAuthorize(value = "hasAnyRole('ROLE_PARTNER','ROLE_MODERATOR','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
//    @GetMapping("/partner/{carId}")
//    public List<TariffDto> getTariffListByCarId(@PathVariable UUID carId) {
//        return tariffService.getTariffListByCarId(carId);
//    }
//
//    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
//    @PostMapping
//    public Result addTariff(@RequestBody TariffDto tariffDto) {
//        return tariffService.addTariff(tariffDto);
//    }
//
//    @PreAuthorize(value = "hasAnyRole('ROLE_PARTNER','ROLE_MODERATOR','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
//    @PutMapping("/{id}")
//    public HttpEntity<?> updateTariff(@PathVariable Integer id, @RequestBody TariffDto tariffDto) {
//        Result result = tariffService.updateTariff(id, tariffDto);
//        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
//    }
//
//    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
//    @DeleteMapping("/{id}")
//    public HttpEntity<?> deleteTariffById(@PathVariable Integer id) {
//        Result result = tariffService.deleteTariffById(id);
//        return ResponseEntity.status(result.isSuccess() ? 200 : 404).body(result);
//    }
//}

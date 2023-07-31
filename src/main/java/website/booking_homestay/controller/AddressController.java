package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.DistrictDTO;
import website.booking_homestay.DTO.ProvinceDTO;
import website.booking_homestay.DTO.WardDTO;
import website.booking_homestay.entity.Districts;
import website.booking_homestay.entity.Provinces;
import website.booking_homestay.entity.Wards;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.DistrictsRepository;
import website.booking_homestay.repository.ProvincesRepository;
import website.booking_homestay.repository.WardsRepository;
import website.booking_homestay.utils.MessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/address")
@RequiredArgsConstructor
public class AddressController {
    private final ProvincesRepository provincesRepository;
    private final DistrictsRepository districtsRepository;
    private final WardsRepository wardsRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces(){
        List<Provinces> provinces = provincesRepository.findAll();
        List<ProvinceDTO> provinceDTOs = provinces.stream().map(province -> modelMapper.map(province,ProvinceDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(provinceDTOs);
    }
    @GetMapping("/districts")
    public ResponseEntity<?> getDistricts(@RequestParam(name = "id",defaultValue = "0") Long provinceId){
        List<Districts> districts = new ArrayList<>();
        List<DistrictDTO> districtDTOs;
        if (provinceId == 0) {
            districts = districtsRepository.findAll();
            districtDTOs = districts.stream().map(district -> modelMapper.map(district,DistrictDTO.class)).collect(Collectors.toList());
            return ResponseEntity.ok(districtDTOs);
        }
        Provinces province = provincesRepository.findById(provinceId).get();
        districts = province.getDistricts();
        districtDTOs = districts.stream().map(district -> modelMapper.map(district,DistrictDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(districtDTOs);
    }
    @GetMapping("/wards")
    public ResponseEntity<?> getWards(@RequestParam(name = "id",defaultValue = "0") Long districtId){
        List<Wards> wards = new ArrayList<>();
        List<WardDTO> wardDTOs;
        if (districtId == 0) {
            wards = wardsRepository.findAll();
            wardDTOs = wards.stream().map(ward -> modelMapper.map(ward,WardDTO.class)).collect(Collectors.toList());
            return ResponseEntity.ok(wardDTOs);
        }
        Districts district = districtsRepository.findById(districtId).get();
        wards = district.getWards();
        wardDTOs = wards.stream().map(ward -> modelMapper.map(ward,WardDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(wardDTOs);
    }
    @GetMapping("/provinces/{id}")
    public ResponseEntity<?> getProvinceDetails(@PathVariable("id") Long provinceId){
        Provinces province = provincesRepository.findById(provinceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, MessageResponse.NOT_FOUND));
        return ResponseEntity.ok(province);
    }
    @GetMapping("/districts/{id}")
    public ResponseEntity<?> getDistrictDetails(@PathVariable("id") Long districtId){
        Districts district = districtsRepository.findById(districtId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, MessageResponse.NOT_FOUND));
        return ResponseEntity.ok(district);
    }
    @GetMapping("/wards/{id}")
    public ResponseEntity<?> getWardDetails(@PathVariable("id") Long wardId){
        Wards ward = wardsRepository.findById(wardId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, MessageResponse.NOT_FOUND));
        return ResponseEntity.ok(ward);
    }


}

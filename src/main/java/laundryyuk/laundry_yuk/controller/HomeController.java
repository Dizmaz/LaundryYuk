package laundryyuk.laundry_yuk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "home/index"; // pastikan ini adalah halaman utama Anda
    }

}

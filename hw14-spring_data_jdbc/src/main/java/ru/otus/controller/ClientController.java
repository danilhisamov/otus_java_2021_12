package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.ClientService;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public String clientListView(Model model) {
        var clients = clientService.getClients();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/create")
    public String clientCreateView(Model model) {
        var client = new Client();
        client.getPhones().add(new Phone());
        model.addAttribute("client", client);
        return "clientCreate";
    }

    @PostMapping("/create")
    public String clientCreate(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }
}

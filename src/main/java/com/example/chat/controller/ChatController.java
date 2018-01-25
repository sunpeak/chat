package com.example.chat.controller;

import com.example.chat.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gg on 2018/1/24.
 */
@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SessionRegistry sessionRegistry;

    @MessageMapping("/chat.{touser}")
    public void chat(@DestinationVariable(value = "touser") String touser, Principal principal, String msg) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/message", "<div class=\"text-right\"><small>[" + DateUtil.now() + "]</small><br/>" + msg.replace("\n", "<br/>") + "<br/><br/></div>");
        Arrays.stream(touser.split(",")).forEach(user ->
                simpMessagingTemplate.convertAndSendToUser(user, "/queue/message", "<div><small>" + principal.getName() + " [" + DateUtil.now() + "]</small><br/>" + msg.replace("\n", "<br/>") + "<br/><br/></div>"));

    }

    @RequestMapping("/users")
    @ResponseBody
    public String getUsers(Principal principal) {
        Set<String> userSet = sessionRegistry.getAllPrincipals().stream().filter(user -> {
            User target = (User) user;
            return !target.getUsername().equals(principal.getName());
        }).map(user -> ((User) user).getUsername()).collect(Collectors.toSet());

        return String.join(",", userSet);
    }

}

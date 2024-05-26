package kafka.producer.resource;

import kafka.producer.dto.PessoaDto;
import kafka.producer.dto.PessoaListDto;
import kafka.producer.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-kafka")
@RequiredArgsConstructor
public class MessageResource {

    private final MessageService messageService;

//    @PostMapping()
//    public ResponseEntity<String> sendMessage(@RequestBody String message) {
//        messageService.sendMessage(message);
//        return ResponseEntity.ok().body(message);
//    }

    @PostMapping(value = "/pessoa")
    public ResponseEntity<PessoaDto> sendPessoa(@RequestBody PessoaDto pessoaDto) {
        messageService.sendPessoa(pessoaDto);
        return ResponseEntity.ok().body(pessoaDto);
    }

    @PostMapping(value = "/pessoa-list")
    public ResponseEntity<List<PessoaDto>> sendPessoaList(@RequestBody PessoaListDto pessoaList) {
        pessoaList.list().forEach(messageService::sendPessoa);
        return ResponseEntity.ok().body(pessoaList.list());
    }
}

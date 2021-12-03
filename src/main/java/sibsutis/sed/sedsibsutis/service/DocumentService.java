package sibsutis.sed.sedsibsutis.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;


@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

    @SneakyThrows
    public byte[] get(byte[] document) throws GeneralSecurityException, IOException, OperatorCreationException, CMSException {

//        Зашифрованный документ private ключем


        return null;
    }

}

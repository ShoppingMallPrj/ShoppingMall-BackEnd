package com.example.mall.IpBan;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class IpBanService {
    private final IpBanRepository ipBanRepository;

    public String getIpAdress(HttpServletRequest request){

        //ip 주소를 얻어온다(여러 종류의 헤더를 적용)
        String ip = request.getHeader("X-Forwarded-For");
        System.out.println("X-Forwarded-For " + ip);
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy " + ip);

        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP " + ip);

        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR " + ip);

        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr " + ip);

        }

        log.info("[{}], Result : IP Address : {}", MDC.get("UUID"),ip);

        return ip;
    }

    public boolean isIpBanned(HttpServletRequest request){
        String ipAddress = getIpAdress(request);
        return ipBanRepository.existsByIp(ipAddress);
    }

    @Transactional
    public void saveBanIp(String ipAddress){
        if (!ipBanRepository.existsByIp(ipAddress)){
            IpBan ipBan = new IpBan();
            ipBan.setIp(ipAddress);
            ipBanRepository.save(ipBan);
        }
    }
}

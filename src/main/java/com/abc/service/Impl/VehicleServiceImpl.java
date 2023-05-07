package com.abc.service.Impl;

import com.abc.dao.VehicleDao;
import com.abc.domian.Vehicle;

import com.abc.domian.ms;
import com.abc.service.VehicleService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired//自动装配
    /* @Qualifier("")有多个bean需要使用这个里面加上想要注入的bean名称*/
    private VehicleDao vehicleDao;


//车辆注册生成签名
    public int insertAll() {
        int rBits = 160;
        int qBits = 512;
        TypeACurveGenerator Apg=new TypeACurveGenerator(rBits,qBits);
        //生成配对参数
        PairingParameters typeAParams=Apg.generate();
        //初始化一个Pairing实例
        Pairing pairing= PairingFactory.getPairing(typeAParams);
        //声明一个群和zq
        Field G1=pairing.getG1();
        Field Zq=pairing.getZr();
        //声明这个群的生成元
        Element P=G1.newRandomElement().getImmutable();
        //用户的密钥和id
        Element r=Zq.newRandomElement().getImmutable();
        Element s=Zq.newRandomElement().getImmutable();
        Element x=Zq.newRandomElement().getImmutable();
        Element u=Zq.newRandomElement().getImmutable();
        Element R=P.mulZn(r);
        Element X=P.mulZn(x);
        Element U=P.mulZn(u);
        //哈希函数
        byte[] h1=R.toBytes();
        Element H1=pairing.getZr().newRandomElement().setFromHash(h1,0,h1.length).getImmutable();
        byte[] h3=X.toBytes();
        Element H3=pairing.getZr().newRandomElement().setFromHash(h3,0,h3.length).getImmutable();
        byte[] h4=U.toBytes();
        Element H4=pairing.getZr().newRandomElement().setFromHash(h4,0,h4.length).getImmutable();
        Element element = s.mulZn(H1);
        Element d = r.add(element);
        Element s2=r;
        Element delta=d.add(x.mulZn(H3)).add(u.mulZn(H4));
        return 1;
    }

    public int selectByName(String name) {
        int rBits = 160;
        int qBits = 512;
        TypeACurveGenerator Apg=new TypeACurveGenerator(rBits,qBits);
        //生成配对参数
        PairingParameters typeAParams=Apg.generate();
        //初始化一个Pairing实例
        Pairing pairing= PairingFactory.getPairing(typeAParams);
        //声明一个群和zq
        Field G1=pairing.getG1();
        Field Zq=pairing.getZr();
        //声明这个群的生成元
        Element P=G1.newRandomElement().getImmutable();
        //用户的密钥和id
        Element r=Zq.newRandomElement().getImmutable();
        Element s=Zq.newRandomElement().getImmutable();
        Element x=Zq.newRandomElement().getImmutable();
        Element u=Zq.newRandomElement().getImmutable();
        Element R=P.mulZn(r);
        Element X=P.mulZn(x);
        Element U=P.mulZn(u);
        Element Pub=P.mulZn(s);
        //哈希函数
        byte[] h1=R.toBytes();
        Element H1=pairing.getZr().newRandomElement().setFromHash(h1,0,h1.length).getImmutable();
        byte[] h3=X.toBytes();
        Element H3=pairing.getZr().newRandomElement().setFromHash(h3,0,h3.length).getImmutable();
        byte[] h4=U.toBytes();
        Element H4=pairing.getZr().newRandomElement().setFromHash(h4,0,h4.length).getImmutable();
        Element element = s.mulZn(H1);
        Element d = r.add(element);
        Element s2=r;
        Element delta=d.add(x.mulZn(H3)).add(u.mulZn(H4));
        Element p=R.add(Pub.mulZn(H1)).add(X.mulZn(H3)).add(U.mulZn(H4));
        if(P.mulZn(delta).equals(p)){
            System.out.println("...");
            return 1;
        }
        return 0;
    }

    public int sendmessg(String name,String message) {
        int rBits = 160;
        int qBits = 512;
        TypeACurveGenerator Apg=new TypeACurveGenerator(rBits,qBits);
        //生成配对参数
        PairingParameters typeAParams=Apg.generate();
        //初始化一个Pairing实例
        Pairing pairing= PairingFactory.getPairing(typeAParams);
        //声明一个群和zq
        Field G1=pairing.getG1();//群
        Field Zq=pairing.getZr();//zp
        ms ms = ms();
        Element w = ms.getPk1().add(ms.getPk2()).add(ms.getPub().mulZn(ms.getY()));
        Element add = ms.getA().mulZn(ms.getY()).add(w);
        Element d=Zq.newRandomElement().getImmutable();
        Element D = ms.getP().mulZn(d);
        byte[] h1=D.toBytes();
        Element sek=pairing.getZr().newRandomElement().setFromHash(h1,0,h1.length).getImmutable();
        byte[] bytes = message.getBytes();
        Element Hm=pairing.getZr().newRandomElement().setFromHash(bytes,0,bytes.length).getImmutable();
        Element element = sek.mulZn(Hm);
        message=element.toString();
        System.out.println(name+"发送的消息是："+message);
        return 1;
    }

    public static ms ms(){

        int rBits = 160;
        int qBits = 512;
        TypeACurveGenerator Apg=new TypeACurveGenerator(rBits,qBits);
        //生成配对参数
        PairingParameters typeAParams=Apg.generate();
        //初始化一个Pairing实例
        Pairing pairing= PairingFactory.getPairing(typeAParams);
        //声明一个群和zq
        Field G1=pairing.getG1();
        Field Zq=pairing.getZr();

        Element P=G1.newRandomElement().getImmutable();
        //服务器注册
        Element s=Zq.newRandomElement().getImmutable();
        Element pub=P.mulZn(s);
        Element sk1=Zq.newRandomElement().getImmutable();
        Element idm=Zq.newRandomElement().getImmutable();
        Element pk1=P.mulZn(sk1);
        //ru对用户生成密钥
        Element sk2=Zq.newRandomElement().getImmutable();
        Element pk2=P.mulZn(sk2);
        byte[] h1=idm.toBytes();
        Element Hm=pairing.getZr().newRandomElement().setFromHash(h1,0,h1.length).getImmutable();
        //随机选a，b
        Element a=Zq.newRandomElement().getImmutable();
        Element b=Zq.newRandomElement().getImmutable();
        Element A = P.mulZn(a);
        Element B = P.mulZn(b);
        Element element = s.mulZn(Hm);
        Element y = sk2.add(element);
        Element sigma = a.mulZn(Hm).add(y).add(sk1);
        ms ms = new ms(B, A, sigma,pk1,pk2,pub,Hm,P);
        return ms;
    }

}

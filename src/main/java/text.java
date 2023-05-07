import com.abc.domian.Vehicle;
import com.abc.domian.ms;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.math.BigInteger;

public class text {
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
        //模拟服务器进行注册
        //Vehicle vehicle = vehicleDao.selectAll(name);//获得用户信息
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
    public static void main(String[] args) {
        int rBits = 160;
        int qBits = 512;
        TypeACurveGenerator Apg=new TypeACurveGenerator(rBits,qBits);
        //生成配对参数
        PairingParameters typeAParams=Apg.generate();
        //初始化一个Pairing实例
        Pairing pairing= PairingFactory.getPairing(typeAParams);
        //声明一个群和zq
        Field Zq=pairing.getZr();
        ms ms = ms();
        Element w = ms.getPk1().add(ms.getPk2()).add(ms.getPub().mulZn(ms.getY()));
        Element add = ms.getA().mulZn(ms.getY()).add(w);
        if(add.isEqual(ms.getP().mulZn(ms.getSigma()))){
            Element d=Zq.newRandomElement().getImmutable();
            Element D = ms.getP().mulZn(d);
            byte[] h1=D.toBytes();
            Element sek=pairing.getZr().newRandomElement().setFromHash(h1,0,h1.length).getImmutable();
            System.out.println(sek);
            System.out.println(D);
            System.out.println("0");
        }
        else {
            System.out.println("1");
        }




    }

}

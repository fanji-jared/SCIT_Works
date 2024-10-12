import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ATM 系统操作类 <br>
 * 1. 假设此 ATM 可以提供无限的纸质货币<br>
 * 2. 假设此 ATM 可以提供各种类型的货币
 * 3. 默认 存取款操作 为原子操作，且 存取款数量 以输入值代替
 * @author fanji
 * @since 2024-9-28
 */
public class ATMSystem {
    public static final Scanner sc = new Scanner(System.in);
    private final String cardNum; // 卡号
    private final String passWord; // 密码
    private final int maxLockCount; // 最大重试次数
    private boolean isLogin = false; // 登录状态
    private int retryCount = 0; // 重试次数
    private MyCurrency myCurrency = new MyCurrency(); // 货币和余额
    private MyCurrency.ExchangeRate exchangeRate;

    /**
     * 常规-构造函数
     * @param cardNum 卡号
     * @param passWord 密码
     */
    public ATMSystem(String cardNum, String passWord, MyCurrency.ExchangeRate exchangeRate) {
        this(cardNum, passWord, 3, exchangeRate);
    }

    /**
     * 自定义最大重试次数-构造函数
     * @param cardNum 卡号
     * @param passWord 密码
     * @param maxLockCount 最大重试次数
     */
    public ATMSystem(String cardNum, String passWord, int maxLockCount, MyCurrency.ExchangeRate exchangeRate) {
        this.cardNum = cardNum;
        this.passWord = passWord;
        this.maxLockCount = maxLockCount;
        this.exchangeRate = exchangeRate;
        this.loginView();
    }

    /**
     * 刷新 CMD <br>
     * 2. 使用 ANSI 转义序列：<br>
     * \033 是 ASCII 码中的 Escape 字符，用于表示后面的字符序列是控制码 <br>
     * [H 表示将光标移动到屏幕左上角（Home 位置）<br>
     * [2J 表示清除屏幕内容，从当前光标位置到屏幕末尾
     */
    public void clsCMD() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }

    /**
     * 用键盘输入选择一个货币名称
     * @return void
     */
    private MyCurrency.Type scCurenType(){
        try {
            int i = 0;
            ArrayList<MyCurrency.Type> tal = new ArrayList<>();
            for(MyCurrency.Type t : MyCurrency.Type.values()) {
                System.out.println("[ " + (i + 1) + " ]." + t.getName());
                tal.add(t);
                i++;
            }
            return tal.get(sc.nextInt() - 1);
        } catch (Exception e) {
            System.out.println(">> 输入有误！");
            this.menuView();
        }
        return null;
    }

    /**
     * 登录系统页面
     */
    private void loginView(){
        this.clsCMD();
        if(this.isLogin) {
            this.menuView();
        }else{
            this.retryCount += 1;
            if(this.retryCount <= this.maxLockCount){
                System.out.println("****** 登录 ATM 系统 ******");
                System.out.println("> 卡号：" + cardNum);
                System.out.println("> 请输入密码：");
                String passWordLogin = sc.next();
                if (passWordLogin.equals(this.passWord)){
                    this.isLogin = true;
                }else{
                    System.out.println("(" + this.retryCount + " / " + this.maxLockCount + ") 密码错误，登录系统失败！");
                }
                this.loginView();
            }else {
                System.out.println("(" + this.retryCount + " / " + this.maxLockCount + ") 登录次数过多，已锁定账户！");
            }
        }
    }

    /**
     * 菜单页面 <br>
     * 查询、取款、退出（多加 存款、兑换货币）
     */
    private void menuView(){
        this.clsCMD();
        if(this.isLogin) {
            System.out.println("****** ATM 系统菜单 ******");
            System.out.println("> 当前卡号：" + cardNum);
            System.out.println("[ 1 ]. 查询");
            System.out.println("[ 2 ]. 存款");
            System.out.println("[ 3 ]. 取款");
            System.out.println("[ 4 ]. 兑换");
            System.out.println("[ anyInt ]. 退出");
            switch (sc.nextInt()){
                case 1:
                    this.selectBalanceView();
                    break;
                case 2:
                    this.setBalanceView();
                    break;
                case 3:
                    this.getBalanceView();
                    break;
                case 4:
                    this.convertBalanceView();
                    break;
                default:
                    System.out.println("感谢使用此系统！");
            }
        }else{
            this.loginView();
        }
    }

    /**
     * 查询账户指定货币的余额 <br>
     * 查询和取款默认分别可以选择两个币种（人民币、美元）进行操作，建议各有10000元
     */
    private void selectBalanceView(){
        this.clsCMD();
        if(this.isLogin) {
            System.out.println("****** 请输入需要查询的货币名称的编号：******");
            MyCurrency.Type MT = this.scCurenType();
            MyCurrency.Bean MB = myCurrency.getBeanByType(MT);
            System.out.println(">> 卡号：" + cardNum);
            System.out.println(">> 货币名称：" + MB.getName());
            System.out.println(">> 数量：" + MB.getValue());
            System.out.println("****** 请输入下一步操作编号： ******");
            System.out.println("[ 1 ]. 继续查询");
            System.out.println("[ anyInt ]. 返回菜单");
            if (sc.nextInt() == 1){
                this.selectBalanceView();
            }else{
                this.menuView();
            }
        }else{
            this.loginView();
        }
    }

    /**
     * 存入账户指定货币和数量 <br>
     * 查询和取款默认分别可以选择两个币种（人民币、美元）进行操作，建议各有10000元
     */
    private void setBalanceView(){
        this.clsCMD();
        if(this.isLogin) {
            System.out.println("****** 存款 ******");
            System.out.println("> 请输入需要存入的货币名称的编号：");
            MyCurrency.Type MT = this.scCurenType();
            MyCurrency.Bean mb = new MyCurrency.Bean();
            mb.setName(MT);
            System.out.println("> 请输入存入金额（double）：");
            mb.setValue(sc.nextDouble());
            myCurrency.add(mb);
            System.out.println("****** 请输入下一步操作编号： ******");
            System.out.println("[ 1 ]. 继续存款");
            System.out.println("[ anyInt ]. 返回菜单");
            if (sc.nextInt() == 1){
                this.setBalanceView();
            }else{
                this.menuView();
            }
        }else{
            this.loginView();
        }
    }

    /**
     * 取出账户指定数量的货币 <br>
     * 查询和取款默认分别可以选择两个币种（人民币、美元）进行操作，建议各有10000元
     */
    private void getBalanceView(){
        this.clsCMD();
        if(this.isLogin) {
            System.out.println("****** 取款 ******");
            System.out.println("> 请输入需要取出的货币名称的编号：");
            ArrayList<MyCurrency.Bean> bal = myCurrency.beanArrayList;
            if(!bal.isEmpty()){
                MyCurrency.Type MT = this.scCurenType();
                for (MyCurrency.Bean bean: bal) {
                    if (bean.getName() == MT){
                        System.out.println("> 请输入取出金额（double）：");
                        double qu = sc.nextDouble();
                        if (bean.getValue() >= qu){
                            bean.setValue(bean.getValue() - qu);
                            System.out.println("****** 请输入下一步操作编号： ******");
                            System.out.println("[ 1 ]. 继续取款");
                            System.out.println("[ anyInt ]. 返回菜单");
                            if (sc.nextInt() == 1){
                                this.getBalanceView();
                            }else{
                                this.menuView();
                            }
                        }else{
                            System.out.println("存款过低！");
                            this.setBalanceView();
                        }
                    }
                }
                this.getBalanceView();
            }else{
                System.out.println("没有存款！");
                this.setBalanceView();
            }
        }else{
            this.loginView();
        }
    }

    /**
     * 转换账户指定货币为另一种货币
     */
    private void convertBalanceView(){
        this.clsCMD();
        if(this.isLogin) {
            System.out.println("****** 兑换货币 ******");
            ArrayList<MyCurrency.Bean> bal = myCurrency.beanArrayList;
            if(!bal.isEmpty()) {
                System.out.println("> 请输入[源货币]名称的编号：");
                MyCurrency.Type sourceType = this.scCurenType();
                MyCurrency.Bean sourceBean = myCurrency.getBeanByType(sourceType);
                System.out.println("> 请输入[需要兑换的货币]名称的编号：");
                MyCurrency.Type targetType = this.scCurenType();
                MyCurrency.Bean targetBean = myCurrency.getBeanByType(targetType);
                System.out.println("> 请输入需要兑换的数量(double)：");
                double amount = sc.nextDouble();
                double target = exchangeRate.Converter(amount, sourceType, targetType);
                sourceBean.setValue(sourceBean.getValue() - amount);
                targetBean.setValue(targetBean.getValue() - target);
                System.out.println("****** 请输入下一步操作编号： ******");
                System.out.println("[ 1 ]. 继续兑换");
                System.out.println("[ anyInt ]. 返回菜单");
                if (sc.nextInt() == 1){
                    this.convertBalanceView();
                }else{
                    this.menuView();
                }
            }else{
                System.out.println("没有存款！");
                this.setBalanceView();
            }
        }else{
            this.loginView();
        }
    }
}
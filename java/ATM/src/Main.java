/*
模拟银行ATM取款的流程，完成功能如下：
1、验证密码，3次失败锁定；如果成功，进入系统
2、系统功能为 查询、取款、退出（多加 存款、兑换货币）
3、查询和取款分别可以选择两个币种（人民币、美元）进行操作，建议各有10000元
4、查询和取款完毕，用户可以选择继续操作还是返回上一级
5、在主菜单中，用户选择退出则谢谢使用，结束程序
 */
public class Main {
    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem("1234567890", "123abc", (amount, sourceType, targetType) -> {
//            2024年10月4日 UTC 21:00
            if(sourceType == MyCurrency.Type.USD && targetType == MyCurrency.Type.CNY){
                return amount * 7.02;
            } else if (sourceType == MyCurrency.Type.CNY && targetType == MyCurrency.Type.USD) {
                return amount * 0.14;
            }else {
                return amount;
            }
        });
    }
}
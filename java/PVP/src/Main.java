import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static Random ra = new Random();

    /**
     * 刷新 CMD <br>
     * 2. 使用 ANSI 转义序列：<br>
     * \033 是 ASCII 码中的 Escape 字符，用于表示后面的字符序列是控制码 <br>
     * [H 表示将光标移动到屏幕左上角（Home 位置）<br>
     * [2J 表示清除屏幕内容，从当前光标位置到屏幕末尾
     */
    public static void clsCMD() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Player one = createPlayer();
        Player two = createPlayer();
        System.out.println("****** 开始PK ******");
        Play(one, two);
    }

    private static Player createPlayer() {
        System.out.println("> 创建角色：");
        Player temp = new Player();
        System.out.println("  姓名 (String)：");
        temp.setName(sc.next());
        System.out.println("  血量 (int)：");
        temp.setHP(sc.nextInt());
        System.out.println("  攻击力 (int)：");
        temp.setAttackNum(sc.nextInt());
        clsCMD();
        return temp;
    }

    public static void Play(Player one, Player two){
        if (one.getHP() > 0 && two.getHP() > 0){
            if(ra.nextBoolean()){
                one.Attack(two);
            }else{
                two.Attack(one);
            }
            Play(one, two);
        } else if (one.getHP() == two.getHP()){
            System.out.println("玩家 【" + one.getName() + "】 与 【" + two.getName() + "】 同归于尽！");
        }else{
            System.out.println("玩家【" + (one.getHP() > 0 ? one.getName(): two.getName()) + "】获胜！");
        }
    }
}
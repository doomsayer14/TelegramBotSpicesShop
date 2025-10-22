package yes.shef.telegramshop.util;

/**
 * Class for constants.
 */
public final class Commands {
    private Commands() {}
    /**
     * Static text.
     * If variable ends with "_COMMAND" it means it represents a button with the same name.
     * If variable ends with "_MESSAGE" it means that this text will be sent to a user as an answer.
     */

    public static final String START_COMMAND = "/start";

    public static final String START_MESSAGE = "Привіт! Мене звати Ірина - авторка блогу @iryna_yesman_food, кулінарка, творчиня смаків і засновниця лінійки спецій YESChef \uD83C\uDF3F\n" +
            "\n" +
            "Тут усе  про справжні смаки, домашню кухню з характером та натхнення готувати з любов’ю \uD83D\uDC9B\n" +
            "Я вірю, що спеції можуть більше, ніж просто додати аромату, вони створюють настрій, історії, традиції. Саме тому народився бренд YESChef - натуральні авторські суміші спецій для тих, хто любить готувати смачно, просто і з душею.\n" +
            "\n" +
            "Тут ви можете обрати набори спецій, які імпонуватимуть саме вам і чарівний YESChef-бот допоможе ✨";

    public static final String CATALOGUE_COMMAND = "Каталог товарів";

    public static final String MY_CART_COMMAND = "Мій кошик";

    public static final String TERMS_OF_USE_COMMAND = "Правила доставки";

    public static final String TERMS_OF_USE_MESSAGE = "Правила доставки оферта договір бла бла бла";

    public static final String ABOUT_ME_COMMAND = "Про мене";

    public static final String ABOUT_ME_MESSAGE = "Текст про мене";

    public static final String DEFAULT_ANSWER = "Я вас не зрозумів.";

    public static final String GO_TO_MAIN_MENU_COMMAND = "До головного меню";

    public static final String GO_TO_MAIN_MENU_MESSAGE = "\uD83E\uDD51";

    public static final String CATALOGUE_HEADER_MESSAGE = "Тут ви можете ознайомитись з каталогом товарів⤵️";
}

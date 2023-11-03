// package Level;

// import java.util.ArrayList;
// import java.util.List;


// // This class will be accessed by the Final Boss
// // The enhanced map tiles will be added to the list
// // The enhanced map tiles will also have to extend bossliveslistener and implement the functions
// public class BossLivesListenerManager {
//     // List of enhanced map tiles that need to understand what the lives for the boss is
//     // They need to be added to a list and then when the boss lives changes they will be notified
//     private static List<BossLivesListener> enhancedMapListeners = new ArrayList<>();

//     // Add a listener to the arraylist 
//     public static void addEnhancedMapListener(BossLivesListener listener) {
//         if (!enhancedMapListeners.contains(listener)) {
//             enhancedMapListeners.add(listener);
//         }
//     }

//     // this function will be called by the boss when the lives change, and will notify all enhanced map tiles that need it
//     public static void bossLivesChange(int bossLives){
//         for (BossLivesListener listener: enhancedMapListeners){
//             listener.bossLivesChange(bossLives);
//         }

//     }
    
// }

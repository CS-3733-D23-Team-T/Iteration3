package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class SupplyHashMap extends HashMap<String, Image> {
    public SupplyHashMap(){
        //Free Mont Flowers
        this.put("Blushing Beauty Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFBlushingBeautyBouquet.png")).toString()));
        this.put("Elegance Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFEleganceBouquet.png")).toString()));
        this.put("Charming Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCharmingBouquet.png")).toString()));
        this.put("Cherry Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCherryBouquet.png")).toString()));
        this.put("Zen Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFZenBouquet.png")).toString()));
        this.put("What a Delight Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFWhataDelightBouquet.png")).toString()));
        this.put("Girl Power Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFGirlPowerBouquet.png")).toString()));
        this.put("Birds of Paradise Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFMinimalistBouquet.png")).toString()));
        this.put("Minimalist Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFMinimalistBouquet.png")).toString()));
        this.put("So Chic Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFSoChicBouquet.png")).toString()));

        // Blossoms Path
        this.put("Summer Garden Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSummerGardenBouquet.png")).toString()));
        this.put("Easter Lily Arrangement", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPEasterLilyArrangement.png")).toString()));
        this.put("Tulip Trio", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPTulipTrio.png")).toString()));
        this.put("Springtime Garden Basket", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSpringtimeGardenBasket.png")).toString()));
        this.put("Elegant Orchids", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPElegantOrchids.png")).toString()));
        this.put("Cherry Blossom Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPCherryBlossomBouquet.png")).toString()));
        this.put("Winter Garden Centerpiece", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPWinterGardenCenterpiece.png")).toString()));
        this.put("Rose Arrangement", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPRoseArrangement.png")).toString()));
        this.put("Springtime Succulent Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSpringtimeSucculentGarden.png")).toString()));
        this.put("Butterfly Garden Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPButterflyGardenBouquet.png")).toString()));

        // Garden Grace
        this.put("Wonderland Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGWonderlandBouquet.png")).toString()));
        this.put("Sunny Morning Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSunnyMorningBouquet.png")).toString()));
        this.put("Holiday Cheer Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGHolidayCheerBouquet.png")).toString()));
        this.put("Summer Solstice Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSummerSolsticeBouquet.png")).toString()));
        this.put("Snowy Splendor Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSnowySplendorBouquet.png")).toString()));
        this.put("Forest Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGForestBouquet.png")).toString()));
        this.put("Berries Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGBerriesBouquet.png")).toString()));
        this.put("Cozy Nights Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGCozyNightsBouquet.png")).toString()));
        this.put("Icy Elegance Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGIcyEleganceBouquet.png")).toString()));
        this.put("Cheer Up Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGCheerUpBouquet.png")).toString()));

        // Petal Boutique
        this.put("Roses and Lilies", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBRosesandLilies.png")).toString()));
        this.put("Tropical Oasis", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBTropicalOasis.png")).toString()));
        this.put("Spring Fling", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSpringFling.png")).toString()));
        this.put("Vintage Romance", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBVintageRomance.png")).toString()));
        this.put("Succulent Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSucculentGarden.png")).toString()));
        this.put("Enchanted Forest", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBEnchantedForest.png")).toString()));
        this.put("Orchid Delight", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBOrchidDelight.png")).toString()));
        this.put("Sunflower Surprise", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSunflowerSurprise.png")).toString()));
        this.put("Bold and Beautiful", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBBoldandBeautiful.png")).toString()));
        this.put("Garden Party", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBGardenParty.png")).toString()));

        //logos
        this.put("Staples", new Image(Objects.requireNonNull(App.class.getResource("images/supply_request/Staples.png")).toString()));

    }
}
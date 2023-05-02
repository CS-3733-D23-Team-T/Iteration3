package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class FlowerHashMap extends HashMap<String, Image> {
    public FlowerHashMap() {
        //logos
        this.put("Blossom Path", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BlossomPath.png")).toString()));
        this.put("Garden Grace", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GardenGrace.png")).toString()));
        this.put("Tropical Blooms", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TropicalBlooms.png")).toString()));
        this.put("Wildflower Emporium", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WildflowerEmporium.png")).toString()));
        this.put("Petal Boutique", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PetalBoutique.png")).toString()));
        this.put("Free Mont Flowers", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FreeMontFlowers.png")).toString()));

        //Free Mont Flowers
        this.put("Blushing Beauty Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFBlushingBeautyBouquet.png")).toString()));
        this.put("Elegance Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFEleganceBouquet.png")).toString()));
        this.put("Charming Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCharmingBouquet.png")).toString()));
        this.put("Cherry Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCherryBouquet.png")).toString()));
        this.put("Zen Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFZenBouquet.png")).toString()));
        this.put("What a Delight Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFWhataDelightBouquet.png")).toString()));
        this.put("Girl Power Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFGirlPowerBouquet.jpg")).toString()));
        this.put("Birds of Paradise Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFBirdsofParadiseBouquet.png")).toString()));
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

        // Tropical Blooms
        this.put("Utopian Birds", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBUtopianBirds.png")).toString()));
        this.put("Pink Pineapple", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBPinkPineapple.png")).toString()));
        this.put("Blue Hydrangeas", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBBlueHydrangeas.png")).toString()));
        this.put("Orange Lilies", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBOrangeLilies.png")).toString()));
        this.put("Golden Sunflowers", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBGoldenSunflowers.png")).toString()));
        this.put("Purple Passion", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBPurplePassion.png")).toString()));
        this.put("Succulent Surprise", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBSucculentSurprise.png")).toString()));
        this.put("Rainbow Roses", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBRainbowRoses.png")).toString()));
        this.put("Tropical Breeze", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBTropicalBreeze.png")).toString()));
        this.put("Pure White Lilies", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/TBPureWhiteLilies.png")).toString()));

        // Wildflower Emporium
        this.put("Butterfly Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEButterflyGarden.png")).toString()));
        this.put("Sunflower Fields", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WESunflowerFields.png")).toString()));
        this.put("Wildflower Mix", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEWildflowerMix.png")).toString()));
        this.put("Purple Haze", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEPurpleHaze.png")).toString()));
        this.put("Garden Party", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEGardenParty.png")).toString()));
        this.put("Vintage Charm", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEVintageCharm.png")).toString()));
        this.put("Summer Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WESummerBouquet.png")).toString()));
        this.put("Country Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WECountryGarden.png")).toString()));
        this.put("Sunny Day", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WESunnyDay.png")).toString()));
        this.put("Blooming Love", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/WEBloomingLove.png")).toString()));

    }
}

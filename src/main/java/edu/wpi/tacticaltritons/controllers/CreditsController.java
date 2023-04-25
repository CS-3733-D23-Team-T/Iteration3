package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CreditsController {

    @FXML private Accordion accordion;
    @FXML private Text furnitureClickable1;
    @FXML private Text mealClickable1;
    @FXML private Text mealClickable2;
    @FXML private Text mealClickable3;
    @FXML private Text mealClickable4;
    @FXML private Text mealClickable5;
    @FXML private Text mealClickable6;
    @FXML private Text mealClickable7;
    @FXML private Text mealClickable8;
    @FXML private Text mealClickable9;
    @FXML private Text mealClickable10;
    @FXML private Text mealClickable11;
    @FXML private Text mealClickable12;
    @FXML private Text mealClickable13;
    @FXML private Text mealClickable14;
    @FXML private Text mealClickable15;
    @FXML private Text flowerClickable1;
    @FXML private Text flowerClickable2;
    @FXML private Text flowerClickable3;
    @FXML private Text flowerClickable4;
    @FXML private Text flowerClickable5;
    @FXML private Text flowerClickable6;
    @FXML private Text flowerClickable7;
    @FXML private Text flowerClickable8;
    @FXML private Text flowerClickable9;
    @FXML private Text flowerClickable10;
    @FXML private Text flowerClickable11;
    @FXML private Text flowerClickable12;
    @FXML private Text flowerClickable13;
    @FXML private Text flowerClickable14;
    @FXML private Text flowerClickable15;
    @FXML private Text flowerClickable16;
    @FXML private Text flowerClickable17;
    @FXML private Text flowerClickable18;
    @FXML private Text flowerClickable19;
    @FXML private Text flowerClickable20;
    @FXML private Text flowerClickable21;
    @FXML private Text flowerClickable22;
    @FXML private Text flowerClickable23;
    @FXML private Text flowerClickable24;
    @FXML private Text flowerClickable25;
    @FXML private Text flowerClickable26;
    @FXML private Text flowerClickable27;
    @FXML private Text flowerClickable28;
    @FXML private Text flowerClickable29;
    @FXML private Text flowerClickable30;
    @FXML private Text flowerClickable31;
    @FXML private Text flowerClickable32;
    @FXML private Text flowerClickable33;
    @FXML private Text flowerClickable34;
    @FXML private Text flowerClickable35;
    @FXML private Text flowerClickable36;
    @FXML private Text flowerClickable37;
    @FXML private Text flowerClickable38;
    @FXML private Text flowerClickable39;
    @FXML private Text flowerClickable40;
    @FXML private Text flowerClickable41;
    @FXML private Text flowerClickable42;
    @FXML private Text flowerClickable43;
    @FXML private Text flowerClickable44;
    @FXML private Text flowerClickable45;
    @FXML private Text flowerClickable46;
    @FXML private Text flowerClickable47;
    @FXML private Text flowerClickable48;
    @FXML private Text flowerClickable49;
    @FXML private Text flowerClickable50;
    @FXML private Text flowerClickable51;
    @FXML private Text flowerClickable52;
    @FXML private Text flowerClickable53;
    @FXML private Text flowerClickable54;
    @FXML private Text flowerClickable55;
    @FXML private Text flowerClickable56;
    @FXML private Text flowerClickable57;
    @FXML private Text flowerClickable58;
    @FXML private Text flowerClickable59;
    @FXML private Text flowerClickable60;
    @FXML private Text flowerClickable61;
    @FXML private Text flowerClickable62;



    private Map<String, String> furnitureMap = new HashMap<>();
    private Map<String, String> mealMap = new HashMap<>();
    private Map<String, String> flowerMap = new HashMap<>();
    @FXML
    public void initialize() {
        App.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> accordion.setMinWidth(newValue.doubleValue()));
        App.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) -> accordion.setMinHeight(newValue.doubleValue()));

        //furniture request
        furnitureMap.put("Office Depot", "https://www.officedepot.com/");
        furnitureClickable1.addEventHandler(EventType.ROOT, addEventHandler(furnitureClickable1, furnitureMap.get("Office Depot")));

        //meal request
        //logos
        mealMap.put("Pizzeria", "https://www.flaticon.com/free-icon/pizza_169885");
        mealClickable1.addEventHandler(EventType.ROOT, addEventHandler(mealClickable1, mealMap.get("Pizzeria")));
        mealMap.put("Au Bon Pain", "https://commons.wikimedia.org/wiki/File:Au_Bon_Pain_2018_logo.svg");
        mealClickable2.addEventHandler(EventType.ROOT, addEventHandler(mealClickable2, mealMap.get("Au Bon Pain")));
        mealMap.put("Cafe", "https://www.vectorstock.com/royalty-free-vector/starbucks-corporation-logo-vector-43663260");
        mealClickable3.addEventHandler(EventType.ROOT, addEventHandler(mealClickable3, mealMap.get("Cafe")));
        mealMap.put("Pretzel Restaurant", "https://dy5f5j6i37p1a.cloudfront.net/company/logos/158094/original/06b07b83d61811ec83bb37f7e3e9fe81.png");
        mealClickable4.addEventHandler(EventType.ROOT, addEventHandler(mealClickable4, mealMap.get("Pretzel Restaurant")));

        //Pizzeria
        mealMap.put("Pepperoni pizza", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdeWfv9yUXFpTA_43Tbs77kjYqFJb_4lJwFA&usqp=CAU");
        mealClickable5.addEventHandler(EventType.ROOT, addEventHandler(mealClickable5, mealMap.get("Pepperoni pizza")));
        mealMap.put("Regular pizza", "https://www.istockphoto.com/photo/cheese-pizza-slice-isolated-on-white-gm172286798-3053042");
        mealClickable6.addEventHandler(EventType.ROOT, addEventHandler(mealClickable6, mealMap.get("Regular pizza")));
        mealMap.put("Water glass", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSrhLRFfRBOBRo3m74aV1fA7mhecEzi_hqGloxBK4mxmweWTAKf");
        mealClickable7.addEventHandler(EventType.ROOT, addEventHandler(mealClickable7, mealMap.get("Water glass")));

        //Au bon pain
        mealMap.put("Sandwich", "https://www.istockphoto.com/photo/grilled-panini-gm105490942-7055297");
        mealClickable8.addEventHandler(EventType.ROOT, addEventHandler(mealClickable8, mealMap.get("Sandwich")));
        mealMap.put("Water bottle", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcStqRBENhTjj05zlxJFGo6Ql7Bg64i_O4xsUiS00ICBQUByNck8");
        mealClickable9.addEventHandler(EventType.ROOT, addEventHandler(mealClickable9, mealMap.get("Water bottle")));

        //Cafe
        mealMap.put("Brownie", "https://en.wikipedia.org/wiki/File:Brownie_transparent.png");
        mealClickable10.addEventHandler(EventType.ROOT, addEventHandler(mealClickable10, mealMap.get("Brownie")));
        mealMap.put("Cookie", "https://www.pngegg.com/en/png-bxypr");
        mealClickable11.addEventHandler(EventType.ROOT, addEventHandler(mealClickable11, mealMap.get("Cookie")));
        mealMap.put("Hot chocolate", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRo4XkWKWOonPIZgRT-AG8uxCf294k85eWd8AMwrZt4lhTfHMS1");
        mealClickable12.addEventHandler(EventType.ROOT, addEventHandler(mealClickable12, mealMap.get("Hot chocolate")));
        mealMap.put("Milk", "https://npr.brightspotcdn.com/dims4/default/7d54103/2147483647/strip/true/crop/1280x853+0+0/resize/1760x1172!/format/webp/quality/90/?url=http%3A%2F%2Fnpr-brightspot.s3.amazonaws.com%2Flegacy%2Fsites%2Fkufm%2Ffiles%2F201412%2Fmilk__public_domain_.png");
        mealClickable13.addEventHandler(EventType.ROOT, addEventHandler(mealClickable13, mealMap.get("Milk")));
        mealMap.put("Coffee", "https://stock.adobe.com/au/images/cofee/554261423");
        mealClickable14.addEventHandler(EventType.ROOT, addEventHandler(mealClickable14, mealMap.get("Coffee")));


        //Pretzel
        mealMap.put("Pretzel", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRaWAixFx_06hq5KnY7H2Ll8Tl4yAyLTRl6FAjfvqNszOxZhcAf");
        mealClickable15.addEventHandler(EventType.ROOT, addEventHandler(mealClickable15, mealMap.get("Pretzel")));


        //flower request
        //Garden Grace
        flowerMap.put("Berries Bouquet", "https://www.ihusibloma.com/en/christmassy-flower-bouquet-with-white-roses-red-berries-greenery-and-ribbon");
        flowerClickable1.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable1, flowerMap.get("Berries Bouquet")));
        flowerMap.put("Cheer Up Bouquet", "https://www.floweramacolumbus.com/flowers/reynoldsburg-oh-florists/delightful-sunshine/");
        flowerClickable2.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable2, flowerMap.get("Cheer Up Bouquet")));
        flowerMap.put("Cozy Night", "https://www.bloomsbythebox.com/blog/diy-flower-how-to-tutorials/bouquet-tutorials/rustic-bouquet-with-dried-flower-accents/");
        flowerClickable3.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable3, flowerMap.get("Cozy Night")));
        flowerMap.put("Forest Bouquet", "https://www.wayfair.com/decor-pillows/pdp/ophelia-co-artificial-bouquet-garden-floral-arrangement-w000334340.html");
        flowerClickable4.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable4, flowerMap.get("Forest Bouquet")));
        flowerMap.put("Icy Elegance", "https://loveisblooming.com/silk-wedding-flowers-blog/silk-wedding-flowers/white-silk-wedding-flowers-with-greenery-part-1-delicate-blooms");
        flowerClickable5.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable5, flowerMap.get("Icy Elegance")));
        flowerMap.put("Snowy Splendor", "https://www.impulsiveflowers.net/portfolio/bridal-bouquet-gallery/53698-white-and-silver-bouquet");
        flowerClickable6.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable6, flowerMap.get("Snowy Splendor")));
        flowerMap.put("Holiday Cheer", "https://www.cascadefloralwholesale.com/ideas-for-creating-gorgeous-holiday-bouquets/");
        flowerClickable7.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable7, flowerMap.get("Holiday Cheer")));
        flowerMap.put("Sunny Morning", "https://www.amazon.com/Falcon-Summer-Sunshine-Bouquet/dp/B003HV7AJ8");
        flowerClickable8.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable8, flowerMap.get("Sunny Morning")));
        flowerMap.put("Summer Solstice", "https://floralheights.com/brooklyn-floral-heights-inc/passion.html");
        flowerClickable9.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable9, flowerMap.get("Summer Solstice")));
        flowerMap.put("Wonderland Bouquet", "https://www.bloomsbythebox.com/blog/diy-flowers-how-to-tutorials/winter-wedding-bouquet-recipe/");
        flowerClickable10.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable10, flowerMap.get("Wonderland Bouquet")));

        //Free Mont Flowers
        flowerMap.put("Winter Garden Centerpiece", "https://flowermag.com/flowers-of-christmas/");
        flowerClickable21.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable21, flowerMap.get("Winter Garden Centerpiece")));
        flowerMap.put("So Chic Bouquet", "https://fortlauderdaleflorist.com/products/sweet-harvest-by-dgm-flowers");
        flowerClickable11.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable11, flowerMap.get("So Chic Bouquet")));
        flowerMap.put("Zen Bouquet", "https://www.wayfair.com/keyword.php?keyword=faux+orchids");
        flowerClickable12.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable12, flowerMap.get("Zen Bouquet")));
        flowerMap.put("Minimalist Bouquet", "https://weddings.blumen.com/flower-blog/tag/lisianthus");
        flowerClickable13.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable13, flowerMap.get("Minimalist Bouquet")));
        flowerMap.put("Birds of Paradise Bouquet", "https://fiftyflowers.com/products/make-a-wish-purple-and-pink-flower-bouquet");
        flowerClickable14.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable14, flowerMap.get("Birds of Paradise Bouquet")));
        flowerMap.put("Blushing Beauty Bouquet", "https://www.bloomnation.com/florist/paradise-flower-market/vibrant-garden-mix/");
        flowerClickable15.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable15, flowerMap.get("Blushing Beauty Bouquet")));
        flowerMap.put("Charming Bouquet", "https://www.floraqueen.com/blog/perfect-flowers-to-pair-sunflowers");
        flowerClickable16.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable16, flowerMap.get("Charming Bouquet")));
        flowerMap.put("Cherry Bouquet", "https://www.bloomnation.com/florist/miss-daisy/red-and-purple-mixed-floral-arrangement-black/");
        flowerClickable17.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable17, flowerMap.get("Cherry Bouquet")));
        flowerMap.put("Elegance Bouquet", "https://www.flowersonyork.com/product/5d672b62aba81/150.00?src=FSNFH");
        flowerClickable18.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable18, flowerMap.get("Elegance Bouquet")));
        flowerMap.put("Girl Power Bouquet", "https://www.teleflora.com/bouquet/bejeweled-beauty-by-teleflora?prodID=P_TEV19-1A&promotion=PSGS15&unique=true&srccode=PS_GS_SSTP&utm_medium=cpc&utm_source=google&utm_campaign=18021711336&device=c&gclid=EAIaIQobChMIkPiDxKPC_gIV9BKzAB2qTAV-EAQYAiABEgLjKvD_BwE");
        flowerClickable19.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable19, flowerMap.get("Girl Power Bouquet")));
        flowerMap.put("What a Delight Bouquet", "https://www.hiattsflorist.com/product/5c23b817ce59d/wishing-bouquet-");
        flowerClickable20.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable20, flowerMap.get("What a Delight Bouquet")));

        //Blossom Path
        flowerMap.put("Tulip Trio", "https://www.vogueflowers.com/tulip-bunches-30-stems-modern-beautiful/");
        flowerClickable22.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable22, flowerMap.get("Tulip Trio")));
        flowerMap.put("Summer Garden Bouquet", "https://www.pinterest.com/pin/321303754657638840/");
        flowerClickable23.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable23, flowerMap.get("Summer Garden Bouquet")));
        flowerMap.put("Springtime Garden Basket", "https://www.bloomnation.com/florist/forever-flowers-3/fabulous-flora-basket-by-forever-flowers/");
        flowerClickable24.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable24, flowerMap.get("Springtime Garden Basket")));
        flowerMap.put("Springtime Succulent Garden", "https://www.1800flowers.com/cactus-dish-garden-1828");
        flowerClickable25.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable25, flowerMap.get("Springtime Succulent Garden")));
        flowerMap.put("Rose Arrangement", "https://www.ftd.com/product/ever-after-rose-bouquet-prd-v1m");
        flowerClickable26.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable26, flowerMap.get("Rose Arrangement")));
        flowerMap.put("Elegant Orchids", "https://flowersonfirst.com/products/regal-orchids");
        flowerClickable27.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable27, flowerMap.get("Elegant Orchids")));
        flowerMap.put("Easter Lily Arrangement", "https://www.conklyns.com/plants/easter-lily-alexandria/");
        flowerClickable28.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable28, flowerMap.get("Easter Lily Arrangement")));
        flowerMap.put("Cherry Blossom Bouquet", "https://www.marthastewart.com/7868988/wedding-ideas-use-cherry-blossoms");
        flowerClickable29.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable29, flowerMap.get("Cherry Blossom Bouquet")));
        flowerMap.put("Butterfly Garden Bouquet", "https://everydayliving.me/2020/08/17/farm-fresh-flowers/");
        flowerClickable30.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable30, flowerMap.get("Butterfly Garden Bouquet")));

        //Wildflower Emporium
        flowerMap.put("Wildflower Mix", "https://www.amazon.com/Bovvered-Artificial-Wildflowers-Arrangements-Decoration/dp/B0BTVX17Z2");
        flowerClickable31.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable31, flowerMap.get("Wildflower Mix")));
        flowerMap.put("Sunflower Fields", "https://www.flowerpetalsinc.com/product/5aef7376606f6/golden-sunflower-bouquet");
        flowerClickable32.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable32, flowerMap.get("Sunflower Fields")));
        flowerMap.put("Butterfly Garden", "https://www.addedtouchfloral.com/bouquet/telefloras-vintage-butterfly-bouquet/p_ef_t21m400a");
        flowerClickable33.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable33, flowerMap.get("Butterfly Garden")));
        flowerMap.put("Vintage Charm", "https://www.bloomnation.com/florist/pedy-s-petals-flower-and-event-design/vintage-bouquet/");
        flowerClickable34.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable34, flowerMap.get("Vintage Charm")));
        flowerMap.put("Purple Haze", "https://www.pinterest.com/pin/413557178254074879/");
        flowerClickable35.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable35, flowerMap.get("Purple Haze")));
        flowerMap.put("Garden Party", "https://www.bataviafloralcreations.com/bouquet/arrive-in-style/p_ef_t55-2a");
        flowerClickable36.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable36, flowerMap.get("Garden Party")));
        flowerMap.put("Sunny Day", "https://www.fromyouflowers.com/products/telefloras_be_happyreg_bouquet.htm");
        flowerClickable37.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable37, flowerMap.get("Sunny Day")));
        flowerMap.put("Country Garden", "https://www.richardsonsflowersandgifts.com/flower-arrangement/lavender-love/prod9111636?skuId=sku8883164&zipMin=");
        flowerClickable38.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable38, flowerMap.get("Country Garden")));
        flowerMap.put("Summer Bouquet", "https://flowersbymarianne.com/hampton-falls-flowers-by-marianne/summer-fun-bouquet.html");
        flowerClickable39.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable39, flowerMap.get("Summer Bouquet")));
        flowerMap.put("Blooming Love", "https://www.pinterest.com/pin/531002612299881954/");
        flowerClickable40.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable40, flowerMap.get("Blooming Love")));

        //Tropical Blooms
        flowerMap.put("Orange Lilies", "https://www.bloomsvilla.com/products/12-orange-asiatic-lily-boquet");
        flowerClickable41.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable41, flowerMap.get("Orange Lilies")));
        flowerMap.put("Blue Hydrangeas", "https://www.burnthillsflorist.com/product/605b5c0dcc0e50.02151/hydrangeas-in-blue");
        flowerClickable42.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable42, flowerMap.get("Blue Hydrangeas")));
        flowerMap.put("Pink Pineapple", "https://www.pinterest.com/pin/82190761935239778/");
        flowerClickable43.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable43, flowerMap.get("Pink Pineapple")));
        flowerMap.put("Utopian Birds", "https://www.enchantedfloristpasadena.com/teleflora-exotic-grace-tropical-flower-arrangement/");
        flowerClickable44.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable44, flowerMap.get("Utopian Birds")));
        flowerMap.put("Golden Sunflowers", "https://urbanstems.com/products/flowers/the-sonny/FLRL-B-00127.html?gclid=EAIaIQobChMI89TwmK7D_gIVA97jBx2aZwtQEAQYBCABEgIlW_D_BwE");
        flowerClickable45.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable45, flowerMap.get("Golden Sunflowers")));
        flowerMap.put("Purple Passion", "https://www.veldkampsflowers.com/flowers/pleasing-purple-bouquet/");
        flowerClickable46.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable46, flowerMap.get("Purple Passion")));
        flowerMap.put("Rainbow Roses", "https://www.fromyouflowers.com/products/one_dozen_mixed_roses.htm");
        flowerClickable47.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable47, flowerMap.get("Rainbow Roses")));
        flowerMap.put("Succulent Surprise", "https://www.gabrielawakeham.com/green-succulents-parchment-roses/");
        flowerClickable48.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable48, flowerMap.get("Succulent Surprise")));
        flowerMap.put("Tropical Breeze", "https://www.pinterest.com/pin/island-breeze-bouquet--477100154282805905/");
        flowerClickable49.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable49, flowerMap.get("Tropical Breeze")));
        flowerMap.put("Pure White Lilies", "https://www.fromyouflowers.com/products/simply_lily_-_white.htm");
        flowerClickable50.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable50, flowerMap.get("Pure White Lilies")));

        //Petal Boutique
        flowerMap.put("Bold and Beautiful", "https://georgewood.com/products/dozen-mutli-color-roses");
        flowerClickable51.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable51, flowerMap.get("Bold and Beautiful:")));
        flowerMap.put("Enchanted Forest", "https://www.bloomsbythebox.com/filler-flowers/gypsophila-xlence_7926/");
        flowerClickable52.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable52, flowerMap.get("Enchanted Forest")));
        flowerMap.put("Garden Party", "https://www.1800flowers.com/charming-garden-bouquet-176342");
        flowerClickable53.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable53, flowerMap.get("Garden Party")));
        flowerMap.put("Orchid Delight", "https://www.igp.com/p-opulent-orchids-bouquet-109604");
        flowerClickable54.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable54, flowerMap.get("Orchid Delight")));
        flowerMap.put("Spring Fling", "https://fortlauderdaleflorist.com/collections/summer-flowers");
        flowerClickable55.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable55, flowerMap.get("Spring Fling")));
        flowerMap.put("Roses and Lilies", "https://freshideasmodesto.com/modesto-fresh-ideas-flower-co/premium-rose-and-lily-vase.html");
        flowerClickable56.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable56, flowerMap.get("Roses and Lilies")));
        flowerMap.put("Succulent Garden", "https://www.mygift.com/products/artificial-succulent-plants-arrangement-in-grey-square-cement-pot");
        flowerClickable57.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable57, flowerMap.get("Succulent Garden")));
        flowerMap.put("Sunflower Surprise", "https://www.suellensflowers.com/product/sunlit-days-bouquet");
        flowerClickable58.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable58, flowerMap.get("Sunflower Surprise")));
        flowerMap.put("Tropical Oasis", "https://www.pinterest.com/pin/144467100533717834/");
        flowerClickable59.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable59, flowerMap.get("Tropical Oasis")));
        flowerMap.put("Vintage Romance", "https://www.mcflor.com/Forever_Love?AspxAutoDetectCookieSupport=1");
        flowerClickable60.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable60, flowerMap.get("Vintage Romance")));

        //Logos
        flowerMap.put("Logos", "https://www.freelogodesign.org/");
        flowerClickable61.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable61, flowerMap.get("Logos")));
        flowerMap.put("Logos", "https://www.freelogodesign.org/");
        flowerClickable62.addEventHandler(EventType.ROOT, addEventHandler(flowerClickable62, flowerMap.get("Logos")));


    }
    private void resize(){
    }
    private EventHandler<? super Event> addEventHandler(Text text, String link){
        return event -> {
            if(event.getEventType() == MouseEvent.MOUSE_ENTERED){
                text.setUnderline(true);
            }
            else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
                text.setUnderline(false);
            }
            else if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(link));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

        };
    }
}

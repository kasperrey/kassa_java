package be.kasperreynders.kaprotest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KassaController {

    private final KassaRepository kassa;

    public KassaController(KassaRepository kassa) {
        this.kassa = kassa;
    }

    @GetMapping("/kassa/producten/prijs")
    public String prijs(@RequestParam(value = "name", defaultValue = "Bil-Bilbos") String name,
                        @RequestParam(value = "password", defaultValue = "") String password) {
        List<Bankkaart> bankkaarten = kassa.getAlleBankkaarten();
        for (Bankkaart b: bankkaarten) {
            if (b.code().equals(password)) {
                List<Product> producten = kassa.getAllProducts();
                String html = String.format("""
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                            <style>
                                body {
                                    margin: 0;
                                    font-family: Arial, Helvetica, sans-serif;
                                }

                                .topnav {
                                    overflow: hidden;
                                    background-color: #333;
                                }

                                .topnav a {
                                    float: left;
                                    color: #f2f2f2;
                                    text-align: center;
                                    padding: 14px 16px;
                                    text-decoration: none;
                                    font-size: 17px;
                                }

                                .topnav a:hover {
                                    background-color: #ddd;
                                    color: black;
                                }

                                .topnav a.active {
                                    background-color: #04AA6D;
                                    color: white;
                                }
                            </style>

                        <div class="topnav">
                            <a href="/?password=%s">Home</a>
                            <a href="/kassa/producten/maken?password=%s">Product maken</a>
                            <a class="active" href="/kassa/producten/prijs?password=%s">Producten prijs</a>
                        </div>
                        """, password, password, password);
                html = html.concat("<select name='producten' id='producten'>\n");
                for (Product p : producten) {
                    html = html.concat(String.format("    <option onclick=\"window.location.assign(%s)\" value='%s'>%s</option>\n",
                            "&quot;prijs?name=" + p.naam() + "&password=" + password + "&quot;", p.naam(), p.naam()));
                }
                html = html.concat("</select>");
                var product = kassa.getProductByName(name);
                if (product.isPresent()) {
                    return String.format(html + "<p>De prijs van %s is %.2f</p>", name, product.get().prijs());
                } else {
                    return String.format(html + "<p>Geen product met de naam '%s'</p>", name);
                }
            }
        }
        return """
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        body {
                            margin: 0;
                            font-family: Arial, Helvetica, sans-serif;
                        }
                        
                        button {
                               background-color: #4CAF50;
                               width: 100%;
                                color: orange;
                                padding: 15px;
                                margin: 10px 0px;
                                border: none;
                                cursor: pointer;
                        }
                        
                        form {
                                border: 3px solid #f1f1f1;
                        }
                        
                        input[type=password] {
                                width: 100%;
                                margin: 8px 0;
                                padding: 12px 20px;
                                display: inline-block;
                                border: 2px solid green;
                                box-sizing: border-box;
                        }
                            
                        button:hover {
                                opacity: 0.7;
                        }
                         
                         .container {
                                padding: 25px;
                                background-color: lightblue;
                         }
                    </style>
                </head>
                <body>
                
                 <form>
                        <div class="container">
                            <label>Password : </label>
                            <input type="password" placeholder="Enter Password" name="password" required>
                            <button type="submit">Login</button>
                        </div>
                    </form>
                </body>""";
    }

    @GetMapping("/kassa/producten/maken")
    public String maken(@RequestParam(value = "naam", defaultValue = "") String naam,
                        @RequestParam(value = "prijs", defaultValue = "0.00") double prijs,
                        @RequestParam(value = "barcode", defaultValue = "0") Long barcode,
                        @RequestParam(value = "password", defaultValue = "") String password) {
        List<Bankkaart> bankkaarten = kassa.getAlleBankkaarten();
        for (Bankkaart b: bankkaarten) {
            if (b.code().equals(password)) {
                String html = String.format("""
                        <!DOCTYPE html>
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                            <style>
                                body {
                                    margin: 0;
                                    font-family: Arial, Helvetica, sans-serif;
                                }

                                .topnav {
                                    overflow: hidden;
                                    background-color: #333;
                                }

                                .topnav a {
                                    float: left;
                                    color: #f2f2f2;
                                    text-align: center;
                                    padding: 14px 16px;
                                    text-decoration: none;
                                    font-size: 17px;
                                }

                                .topnav a:hover {
                                    background-color: #ddd;
                                    color: black;
                                }

                                .topnav a.active {
                                    background-color: #04AA6D;
                                    color: white;
                                }
                            </style>

                        <div class="topnav">
                            <a href="/?password=%s">Home</a>
                            <a class="active" href="/kassa/producten/maken?password=%s">Product maken</a>
                            <a href="/kassa/producten/prijs?password=%s">Producten prijs</a>
                        </div>
                        
                        <div>
                            <input placeholder="naam" id="naam" required>
                            <input placeholder="prijs" id="prijs" required>
                            <input placeholder="barcode" id="barcode" required>
                            <button onclick="f()">maak</button>
                        </div>
                        
                        <script>
                            function f() {
                                    var n = document.getElementById("naam")
                                    var p = document.getElementById("prijs")
                                    var b = document.getElementById("barcode")
                                    window.location.assign("?naam="+n.value+"&prijs="+p.value+"&barcode="+b.value+"&password=%s")
                                }
                        </script>
                        
                        """, password, password, password, password);

                if ((!naam.isEmpty()) && (prijs != 0.00) && (barcode != 0)) {
                    kassa.createProduct(new Product(0, naam, prijs, barcode));
                }

                return html;
            }
        }
        return """
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        body {
                            margin: 0;
                            font-family: Arial, Helvetica, sans-serif;
                        }
                        
                        button {
                               background-color: #4CAF50;
                               width: 100%;
                                color: orange;
                                padding: 15px;
                                margin: 10px 0px;
                                border: none;
                                cursor: pointer;
                        }
                        
                        form {
                                border: 3px solid #f1f1f1;
                        }
                        
                        input[type=password] {
                                width: 100%;
                                margin: 8px 0;
                                padding: 12px 20px;
                                display: inline-block;
                                border: 2px solid green;
                                box-sizing: border-box;
                        }
                            
                        button:hover {
                                opacity: 0.7;
                        }
                         
                         .container {
                                padding: 25px;
                                background-color: lightblue;
                         }
                    </style>
                </head>
                <body>
                
                 <form>
                        <div class="container">
                            <label>Password : </label>
                            <input type="password" placeholder="Enter Password" name="password" required>
                            <button type="submit">Login</button>
                        </div>
                    </form>
                </body>""";
    }
}

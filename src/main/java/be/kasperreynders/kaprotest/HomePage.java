package be.kasperreynders.kaprotest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomePage {

    private final KassaRepository kassa;

    public HomePage(KassaRepository kassa) {
        this.kassa = kassa;
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "password", defaultValue = "") String password) {
        List<Bankkaart> bankkaarten = kassa.getAlleBankkaarten();
        for (Bankkaart b: bankkaarten) {
            if (b.code().equals(password)) {
                return String.format("""
                        <head>
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
                         </head>
                         <body>
                                        
                         <div class="topnav">
                             <a class="active" href="/?password=%s">Home</a>
                             <a href="/kassa/producten/maken?password=%s">Product maken</a>
                             <a href="/kassa/producten/prijs?password=%s">Producten prijs</a>
                         </div>
                                         
                         </body>""", password, password, password);
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

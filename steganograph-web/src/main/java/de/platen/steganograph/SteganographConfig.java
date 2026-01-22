package de.platen.steganograph;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SteganographConfig {

    @Bean
    public Aktionen aktionen() {
        return new Aktionen(
                new AktionVersteckenInBild(),
                new AktionVersteckenInAudio(),
                new AktionHolenAusBild(),
                new AktionHolenAusAudio(),
                new AktionZufallsdatei(),
                new AktionInDatei(),
                new AktionAusDatei());
    }
}

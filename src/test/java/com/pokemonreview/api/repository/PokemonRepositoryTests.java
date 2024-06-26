package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    private PokemonRepository pokemonRepository;

    @Autowired
    public PokemonRepositoryTests(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @Test
    public void PokemonRepository_SaveAll_ReturnsSavedPokemon() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_GetAll_ReturnsMoreThatOnePokemon() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();
        Pokemon pokemon2 = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon2);

        List<Pokemon> pokemonList = pokemonRepository.findAll();

        Assertions.assertThat(pokemonList).isNotNull();
        Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnsPokemonNotNull() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonRepository.save(pokemon);

        Pokemon savedPokemon = pokemonRepository.findById(pokemon.getId()).get();

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonRepository_FindByType_ReturnsPokemonNotNull() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonRepository.save(pokemon);

        Pokemon savedPokemon = pokemonRepository.findByType(pokemon.getType()).get();

        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnsPokemonNotNull() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonRepository.save(pokemon);

        Pokemon savedPokemon = pokemonRepository.findById(pokemon.getId()).get();
        savedPokemon.setType("Electric");
        savedPokemon.setName("Raichu");

        Pokemon updatedPokemon = pokemonRepository.save(savedPokemon);

        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
        Assertions.assertThat(updatedPokemon.getType()).isNotNull();
    }

    @Test
    public void PokemonRepository_DeletePokemon_ReturnsPokemonIsEmpty() {
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        pokemonRepository.save(pokemon);

        pokemonRepository.deleteById(pokemon.getId());
        Optional<Pokemon> returnedPokemon = pokemonRepository.findById(pokemon.getId());

        Assertions.assertThat(returnedPokemon).isEmpty();
    }
}

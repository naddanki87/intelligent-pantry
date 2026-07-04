package com.colruytgroup.intelligentpantry.preference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.azure.cosmos.models.PartitionKey;
import com.colruytgroup.intelligentpantry.controller.PreferenceController;
import com.colruytgroup.intelligentpantry.cosmos.repository.PreferenceRepository;
import com.colruytgroup.intelligentpantry.entity.Preference;
import com.colruytgroup.intelligentpantry.entity.preference.Metadata;
import com.colruytgroup.intelligentpantry.exception.GlobalExceptionHandler;
import com.colruytgroup.intelligentpantry.service.impl.PreferenceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Smoke test for the preferences CRUD slice. Cosmos I/O is mocked; everything
 * else (controller, service, schemaless entity/DTO Jackson mapping, metadata
 * stamping/versioning) runs for real.
 */
@WebMvcTest(PreferenceController.class)
@Import({PreferenceServiceImpl.class, GlobalExceptionHandler.class})
class PreferenceCrudSmokeTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PreferenceRepository repository;

    /**
     * The structure Cosmos persists: fixed id/userId/metadata at the top level,
     * with the schemaless content nested under "preferences". Must round-trip.
     */
    @Test
    void entity_nestsContentUnderPreferences_andRoundTrips() throws Exception {
        Preference pref = Preference.builder()
                .id("p1")
                .userId("u1")
                .metadata(new Metadata("t0", "t0", 1))
                .preferences(new java.util.LinkedHashMap<>(java.util.Map.of(
                        "type", "recipe",
                        "diet", java.util.Map.of("vegetarian", true))))
                .build();

        String json = objectMapper.writeValueAsString(pref);

        // Fixed fields at top level; variable content nested under "preferences".
        assertThat(json).contains("\"id\":\"p1\"");
        assertThat(json).contains("\"userId\":\"u1\"");
        assertThat(json).contains("\"preferences\":");
        assertThat(json).contains("\"metadata\":");

        Preference back = objectMapper.readValue(json, Preference.class);
        assertThat(back.getId()).isEqualTo("p1");
        assertThat(back.getUserId()).isEqualTo("u1");
        assertThat(back.getMetadata().version()).isEqualTo(1);
        assertThat(back.getPreferences()).containsEntry("type", "recipe");
        assertThat(back.getPreferences()).containsKey("diet");
    }

    @Test
    void create_stampsMetadataV1_andEchoesSchemalessBody() throws Exception {
        when(repository.save(any(Preference.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
                { "userId": "u1", "preferences": { "type": "recipe", "diet": { "vegetarian": true } } }
                """;

        mvc.perform(post("/api/preferences").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("u1"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.preferences.type").value("recipe"))
                .andExpect(jsonPath("$.preferences.diet.vegetarian").value(true))
                .andExpect(jsonPath("$.metadata.version").value(1))
                .andExpect(jsonPath("$.metadata.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.metadata.updatedAt").isNotEmpty());
    }

    @Test
    void create_withoutUserId_is400() throws Exception {
        mvc.perform(post("/api/preferences").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"preferences\": { \"type\": \"recipe\" } }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void find_byUserIdAndId_pointReads() throws Exception {
        when(repository.findById(eq("p1"), eq(new PartitionKey("u1"))))
                .thenReturn(Optional.of(sample("u1", "p1")));

        mvc.perform(get("/api/preferences").param("userId", "u1").param("id", "p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("p1"))
                .andExpect(jsonPath("$[0].preferences.type").value("recipe"));
    }

    @Test
    void find_byIdOnly_crossPartition() throws Exception {
        when(repository.findById("p1")).thenReturn(Optional.of(sample("u1", "p1")));

        mvc.perform(get("/api/preferences").param("id", "p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("u1"));
    }

    @Test
    void find_byUserIdOnly_listsAll() throws Exception {
        when(repository.findByUserId("u1")).thenReturn(List.of(sample("u1", "p1"), sample("u1", "p2")));

        mvc.perform(get("/api/preferences").param("userId", "u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void find_withNoCriteria_is400() throws Exception {
        mvc.perform(get("/api/preferences"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_bumpsVersion_preservesCreatedAt() throws Exception {
        Preference existing = Preference.builder()
                .id("p1")
                .userId("u1")
                .metadata(new Metadata("created-0", "updated-0", 1))
                .preferences(new java.util.LinkedHashMap<>(java.util.Map.of("type", "old")))
                .build();

        when(repository.findById(eq("p1"), eq(new PartitionKey("u1")))).thenReturn(Optional.of(existing));
        when(repository.save(any(Preference.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = "{ \"preferences\": { \"type\": \"new\", \"spiceLevel\": \"hot\" } }";

        mvc.perform(put("/api/preferences").param("userId", "u1").param("id", "p1")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferences.type").value("new"))
                .andExpect(jsonPath("$.preferences.spiceLevel").value("hot"))
                .andExpect(jsonPath("$.metadata.version").value(2))
                .andExpect(jsonPath("$.metadata.createdAt").value("created-0"))
                .andExpect(jsonPath("$.metadata.updatedAt").value(org.hamcrest.Matchers.not("updated-0")));
    }

    @Test
    void delete_resolvesThenDeletes() throws Exception {
        when(repository.findById(eq("p1"), eq(new PartitionKey("u1"))))
                .thenReturn(Optional.of(sample("u1", "p1")));

        mvc.perform(delete("/api/preferences").param("userId", "u1").param("id", "p1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_missing_is404() throws Exception {
        when(repository.findById(eq("nope"), any(PartitionKey.class))).thenReturn(Optional.empty());

        mvc.perform(delete("/api/preferences").param("userId", "u1").param("id", "nope"))
                .andExpect(status().isNotFound());
    }

    private static Preference sample(String userId, String id) {
        return Preference.builder()
                .id(id)
                .userId(userId)
                .metadata(new Metadata("t0", "t0", 1))
                .preferences(new java.util.LinkedHashMap<>(java.util.Map.of("type", "recipe")))
                .build();
    }
}

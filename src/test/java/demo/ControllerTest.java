package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.Chainable;
import demo.domain.ChainableBase;
import demo.domain.Leaf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ControllerTest {

    private static final String FOO_HASH = "LCa0a2j_xo_5m0U8HTBBNBNCLXBkg7-g-YpeiGJm564";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testMapA() throws Exception {
        MvcResult result = mockMvc.perform(get("/mapA/add/foo"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        assertEquals(FOO_HASH, content);

        MvcResult result2 = mockMvc.perform(get("/mapA/find/" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content2 = result2.getResponse().getContentAsString();
        assertNotNull(content2);
        assertEquals("foo", content2);

        MvcResult result3 = mockMvc.perform(get("/mapA/verify?entry=foo&hash=" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Boolean b = (Boolean) toObject(result3.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(b);

        MvcResult result4 = mockMvc.perform(get("/mapA"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MapA m = (MapA) toObject(result4.getResponse().getContentAsString(), MapA.class);
        assertNotNull(m);
        assertEquals("foo", m.get(content));
    }

    @Test
    public void testMapB() throws Exception {
        MvcResult result = mockMvc.perform(get("/mapB/add/foo"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);

        MvcResult result2 = mockMvc.perform(get("/mapB/find/" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content2 = result2.getResponse().getContentAsString();
        assertNotNull(content2);
        assertEquals(FOO_HASH, content2);

        MvcResult result3 = mockMvc.perform(get("/mapB/verify?entry=foo&hash=" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Boolean b = (Boolean) toObject(result3.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(b);

        MvcResult result4 = mockMvc.perform(get("/mapB"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MapB m = (MapB) toObject(result4.getResponse().getContentAsString(), MapB.class);
        assertNotNull(m);
        assertEquals(FOO_HASH, m.get(content));
    }

    @Test
    public void testMerkleTree() throws Exception {
        MvcResult result = mockMvc.perform(get("/merkleTree/add/foo"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);

        MvcResult result2 = mockMvc.perform(get("/merkleTree/find/" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Leaf l = (Leaf) toObject(result2.getResponse().getContentAsString(), Leaf.class);
        assertNotNull(l);
        assertEquals(FOO_HASH, l.getHash());

        MvcResult result3 = mockMvc.perform(get("/merkleTree/verify?key=" + content + "&entry=foo"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Boolean b = (Boolean) toObject(result3.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(true);

        MvcResult result4 = mockMvc.perform(get("/merkleTree/verify?key=" + content))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        b = (Boolean) toObject(result4.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertFalse(b);

        MvcResult result5 = mockMvc.perform(get("/merkleTree/verify?entry=foo"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        b = (Boolean) toObject(result5.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertFalse(b);

        MvcResult result6 = mockMvc.perform(get("/merkleTree/verify"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        b = (Boolean) toObject(result6.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(b);

        MvcResult result7 = mockMvc.perform(get("/merkleTree"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String s = result7.getResponse().getContentAsString();
        assertNotNull(s);

        Chainable c = ChainableBase.load(s);
        assertNotNull(c);
        assertEquals(FOO_HASH, c.get(content).getHash());
        assertTrue(c.size() == 1);
        assertTrue(c.verify());
        assertTrue(c.verify(content, "foo"));

        MvcResult result8 = mockMvc.perform(get("/merkleTree/load/4"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        s = result8.getResponse().getContentAsString();
        c = ChainableBase.load(s);
        assertNotNull(c);
        assertEquals(5, c.size());
        assertTrue(c.verify());

        MvcResult result9 = mockMvc.perform(get("/merkleTree/clear"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(0, result9.getResponse().getContentLength());
    }

    @Test
    public void testChain() throws Exception {
        mockMvc.perform(get("/merkleTree/load/5"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult result1 = mockMvc.perform(get("/chain/addBlock"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult result2 = mockMvc.perform(get("/merkleTree/add/foo"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String key = result2.getResponse().getContentAsString();
        assertNotNull(key);

        mockMvc.perform(get("/chain/addBlock"))
                .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MvcResult result3 = mockMvc.perform(get("/chain/verify?key=" + key + "&entry=foo"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Boolean b = (Boolean) toObject(result3.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(true);

        MvcResult result4 = mockMvc.perform(get("/chain/find/" + key))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Leaf l = (Leaf) toObject(result4.getResponse().getContentAsString(), Leaf.class);
        assertNotNull(l);
        assertEquals(FOO_HASH, l.getHash());

        MvcResult result6 = mockMvc.perform(get("/chain/verify"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        b = (Boolean) toObject(result6.getResponse().getContentAsString(), Boolean.class);
        assertNotNull(b);
        assertTrue(b);

        MvcResult result7 = mockMvc.perform(get("/chain"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String s = result7.getResponse().getContentAsString();
        assertNotNull(s);

        Chainable c = ChainableBase.load(s);
        assertNotNull(c);
        assertEquals(6, c.size());
        assertTrue(c.verify());

        assertEquals(FOO_HASH, c.get(key).getHash());
        assertTrue(c.verify(key, "foo"));

        MvcResult result9 = mockMvc.perform(get("/chain/clear"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        assertEquals(0, result9.getResponse().getContentLength());
    }

    private Object toObject(String json, Class clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }
}
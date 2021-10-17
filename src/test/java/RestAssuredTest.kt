import dto.TechDto
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import org.junit.jupiter.api.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestAssuredTest {


    lateinit var requestSpecification: RequestSpecification


    @BeforeAll
    fun setup() {
        requestSpecification = RequestSpecBuilder()
            .setBaseUri("https://616b6de316c3fa00171716c6.mockapi.io")
            .setBasePath("/")
            .setContentType(ContentType.JSON)
            .build()

    }

    @AfterAll
    fun clear() {
        RestAssured.reset()
    }


    @Test
    fun test_returns_200() {
        Given {
            spec(requestSpecification)
        } When {
            get("tecnologias")
        } Then {
            statusCode(HttpStatus.SC_OK)
        }
    }

    @Test
    fun test_returns_json() {
        val expectedItem = TechDto(1, "Java")
        val item: List<TechDto> = Given {
            spec(requestSpecification)
        } When {
            get("tecnologias")
        } Then {
            statusCode(HttpStatus.SC_OK)
        }Extract  {
            body().jsonPath().getList( ".", TechDto::class.java)
        }

        Assertions.assertNotNull(item)
        assert(item.size==10)
        assert(item[0] == expectedItem)

    }


    @Test
    fun test_returns_json_fails() {
        val expectedItem = TechDto(999, "ErrorValue")
        val item: List<TechDto> = Given {
            spec(requestSpecification)
        } When {
            get("tecnologias")
        } Then {
            statusCode(HttpStatus.SC_OK)
        }Extract  {
            body().jsonPath().getList( ".", TechDto::class.java)
        }

        assert((item[0] != expectedItem))

    }

}
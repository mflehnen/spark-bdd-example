import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("classpath:features/silver"),
  glue = Array("org/example/test/steps/shared", "org/example/test/steps/silver"),
  plugin = Array("progress"))
class SilverTestRunner {}

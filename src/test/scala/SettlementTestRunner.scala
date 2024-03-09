import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("classpath:features/settlement"),
  glue = Array("org/example/test/steps/shared", "org/example/test/steps/settlement"),
  plugin = Array("progress"))
class SettlementTestRunner {}
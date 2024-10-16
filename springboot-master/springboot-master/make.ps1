Param(
    [switch]$clean,
    [switch]$compile,
    [switch]$test,
    [switch]$run,
    [switch]$package
)

if($clean -eq $true) {
    Write-Host -ForegroundColor Magenta "> Clean-up previou build artifacts ..."
    ./mvnw clean
}

if($compile -eq $true) {
    Write-Host -ForegroundColor Magenta "> Build the project ..."
    ./mvnw compile
}

if($test -eq $true) {
    Write-Host -ForegroundColor Magenta "> Test the project ..."
    ./mvnw test
}

if($run -eq $true) {
    Write-Host -ForegroundColor Magenta "> Run the project ..."
    Write-Host -ForegroundColor Magenta "> Open a browser at address http://localhost:8080"
    ./mvnw spring-boot:run
}

if($package -eq $true) {
    Write-Host -ForegroundColor Magenta "> Deploy the project and create packages ..."
    ./mvnw clean package
}

if ($clean -eq $false -and $compile -eq $false -and $test -eq $false -and $run -eq $false -and $package -eq $false) {
    Write-Host -ForegroundColor Green "Usage:"

    Write-Host -ForegroundColor Yellow -NoNewline "    - make.ps1"
    Write-Host -ForegroundColor Red -NoNewline " -clean"
    Write-Host -ForegroundColor Gray " ... Clean-up previou build artifacts ..."

    Write-Host -ForegroundColor Yellow -NoNewline "    - make.ps1"
    Write-Host -ForegroundColor Red -NoNewline " -compile"
    Write-Host -ForegroundColor Gray " ... Build the project ..."

    Write-Host -ForegroundColor Yellow -NoNewline "    - make.ps1"
    Write-Host -ForegroundColor Red -NoNewline " -test"
    Write-Host -ForegroundColor Gray " ... Test the project ..."

    Write-Host -ForegroundColor Yellow -NoNewline "    - make.ps1"
    Write-Host -ForegroundColor Red -NoNewline " -run"
    Write-Host -ForegroundColor Gray " ... Run the project ..."

    Write-Host -ForegroundColor Yellow -NoNewline "    - make.ps1"
    Write-Host -ForegroundColor Red -NoNewline " -package"
    Write-Host -ForegroundColor Gray " ... Deploy the project and create packages ..."
}

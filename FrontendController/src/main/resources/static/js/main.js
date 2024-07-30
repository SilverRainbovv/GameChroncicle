window.onload = async function () {
    initYearsSelect();
    initGenreSelect();
    initSortOptions();
    await initPlatfromSelect();
    await initBestOfAllTime();
    await initMostAnticipated();
};

function initSortOptions() {
    let sortSelect = document.getElementById("search-select-id");
    let sortOptions = ["Rating", "Year Relased", "A-Z", "Z-A"];

    initSelectOptions(sortSelect, sortOptions);
}

function initGenreSelect() {
    let genreSelect = document.getElementById("genre-select-id");
    let genres = ["ANY", "FPS", "RPG", "Action"];

    initSelectOptions(genreSelect, genres);
}

async function initPlatfromSelect() {
    let platformSelect = document.getElementById("platform-select-id");
    let platforms;
    try {
        platforms = await getPlatforms();
        platforms.unshift("ANY");
    } catch(Error) {
        platforms = ["ANY", "PC", "Playstation 5", "XBOX Series", "Nintendo Switch"];
    }

    initSelectOptions(platformSelect, platforms);
}

function initYearsSelect() {
    let yearSelect = document.getElementById("year-select-id");
    let minYear = 1958;
    let currentYear = new Date().getFullYear();
    let yearRange = numberRange(minYear, currentYear);
    yearRange.unshift("ANY");

    initSelectOptions(yearSelect, yearRange);
}

function initSelectOptions(selectElement, values) {

    values.forEach(addSelectOption);

    function addSelectOption(value, index, array) {
        let option = document.createElement('option');
        option.text = value;
        option.value = value;
        selectElement.appendChild(option);
    }
}

function numberRange(start, end) {
    return new Array(end - start).fill().map((d, i) => end - i);
}

async function getPlatforms() {

    try {
        const response = await fetch("http://localhost:8080/api/v1/platforms");

        if (!response.ok) {
            throw new Error(`Could not fetch resource: ${getPlatformsUrl}`)
        }

        const data = await response.json();

        const platformNames = Array.from(data)
            .map(data => data.name);

        return platformNames;
    }
    catch (error) {
        console.log(error);
    }
};

async function initMostAnticipated() {
    const mostAnticipatedGamesCollection = document.getElementById('most-anticipated-games-collection')

    try {
        const response = await fetch("http://localhost:8080/api/v1/games/anticipated");

        if (!response.ok) {
            throw new Error(`Could not fetch resource: "http://localhost:8080/api/v1/games/best"`)
        }

        const data = await response.json();

        let collectionRow;

        collectionRow = createNewCollectionRow();

        for(const game of data) {
            let collectionRowElement = createNewCollectionRowElelment();
            let elementImage = createElementImage(game.cover);
            collectionRowElement.appendChild(elementImage);

            let elementName = createElementName(game.name);
            collectionRowElement.appendChild(elementName);

            let elementReleaseDate = createElementReleaseDate(game.releaseDate);
            collectionRowElement.appendChild(elementReleaseDate);

            collectionRow.appendChild(collectionRowElement);
        }
        mostAnticipatedGamesCollection.appendChild(collectionRow);

    } catch (error) {
        console.log(error);
    }
}

async function initBestOfAllTime() {

    const bestGamesCollection = document.getElementById("best-all-time-games")

    try{
        const response = await fetch("http://localhost:8080/api/v1/games/best")

        if (!response.ok) {
            throw new Error(`Could not fetch resource: "http://localhost:8080/api/v1/games/best"`)
        }

        const data = await response.json();

        let collectionRow; rowElementsCounter = 0;

        collectionRow = createNewCollectionRow();

        for(const game of data) {
            if(rowElementsCounter < 5) {
                let collectionRowElement = createNewCollectionRowElelment();

                let elementImage = createElementImage(game.cover);
                collectionRowElement.appendChild(elementImage);

                let elementName = createElementName(game.name);
                collectionRowElement.appendChild(elementName);

                collectionRow.appendChild(collectionRowElement);
                rowElementsCounter++;
            } else {
                bestGamesCollection.append(collectionRow);
                rowElementsCounter = 0;
                collectionRow = createNewCollectionRow();
            }
        }
        bestGamesCollection.appendChild(collectionRow);
    } catch (error) {
        console.log(error);
    }
}

function createElementImage(url){
    let elementImage = document.createElement('img');
    elementImage.setAttribute('class', 'collection-element-img');
    elementImage.setAttribute('src', url);
    return elementImage;
}

function createElementName(name) {
    let elementName = document.createElement('div');
    elementName.setAttribute('class', 'collection-element-name-trending');
    let elementNameContent = document.createTextNode(name);
    elementName.appendChild(elementNameContent);
    return elementName;
}

function createElementReleaseDate(releaseDate) {
    let elementReleaseDate = document.createElement('div');
    elementReleaseDate.setAttribute('class', 'collection-element-release-date');
    let elementReleaseDateContent = document.createTextNode(releaseDate);
    elementReleaseDate.appendChild(elementReleaseDateContent);
    return elementReleaseDate;
}

function createNewCollectionRow() {
    let dataRow = document.createElement('div');
    dataRow.setAttribute('class', 'collection-row');
    return dataRow;
}

function createNewCollectionRowElelment() {
    let rowElement = document.createElement('div');
    rowElement.setAttribute('class', 'collection-element');
    return rowElement;
}
from bs4 import BeautifulSoup
import requests
import re
from collections import Counter

PER_PAGE_NAVIGATION_CLASS = 'seitenweise_navigation'
PAGINATION_CLASS = 'pagination'
MAIN_PAGE_URL = 'https://www.heise.de/thema/https'
SITE_SUFFIX = '?seite='

# this function returns a soup page object
def getPage(url):
    r = requests.get(url)
    data = r.text
    spobj = BeautifulSoup(data, "lxml")
    return spobj

# pushes article soup objects into the passed articles array
def parsePage(soup, articles):
    # Summary:
    # In each page, get the <aside class="recommendations"> elements
    # Inside, search for a <div class="recommendation">
    # Every div of that kind is an article, add it to the articles array passed to the function

    recommendationsAside = soup.find('aside', class_ = 'recommendations')
    recommendations = recommendationsAside.find_all('div', class_ = 'recommendation')

    for rec in recommendations:
        articles.append(rec)

# returns array
def getHeadings(articles):
    # Summary:
    # get all <header> tags in an article, 
    # remove new lines and extra spaces and return the result

    headings = []

    for article in articles:
        heading = article.find('header')
        headingText = heading.text.replace('\n', '').strip()
        headings.append(headingText)
    
    return headings

# returns Dictionary<Word, Count>
def getPerWordCountInHeadings(headings):
    # Summary:
    # All symbol are removed from the article except for the one in the german language and dashes
    # Words are then separated by dashes and spaces and counted

    perWordCount = {}

    for heading in headings:
        #re.sub...
        words = re.sub('[^a-zA-Z äÄöÖüÜß\-\'`]', '', heading).replace('-', ' ').lower().split(' ')
        
        for word in words:
            if len(word) < 3:
                continue
            if not word in perWordCount:
                perWordCount[word] = 1
            else:
                perWordCount[word] += 1

    return perWordCount

# returns array
def getTopNWords(wordCountDict, n):
    # Summary:
    # Using the functionality from collections.Counter get the top 3 keys 
    # in the dictionary sorted by their value and push them to the returned array

    counter = Counter(wordCountDict)
    topNWords = []
    
    for word in counter.most_common(n):
        topNWords.append(word)

    return topNWords


# scraper website: greyhound-data.com
def main():

    articles = []

    mainPage = getPage(MAIN_PAGE_URL)
    navigation = mainPage.find_all('nav', class_ = PER_PAGE_NAVIGATION_CLASS)[0]
    navItemsContainer = mainPage.find('span', class_ = PAGINATION_CLASS)
    nonActivePages = navItemsContainer.find_all('a')
    furtherPages = len(nonActivePages)

    parsePage(mainPage, articles);

    for page in range(1, furtherPages+1):
        furtherPage = getPage(MAIN_PAGE_URL + SITE_SUFFIX + str(page))
        parsePage(furtherPage, articles)

    headings = getHeadings(articles)
    perWordCount = getPerWordCountInHeadings(headings)
    top3Words = getTopNWords(perWordCount, 3)

    print("\nDie meist benutzten 3 Wörter mit mehr als 3 Buchstaben inklusive sind: \n")
    for word in top3Words:
        print("{0}: {1} mal".format(word[0], word[1]))
    print("\n\nWichtig: Nur die Artikeln link die zu HTTPS releavnt sind wurden analyziert!\n")


# main program

if __name__ == '__main__':
    main()
<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a id="readme-top"></a>

<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">

  <h1 align="center">illeGITimate</h1>

</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#what-is-going-on-what-does-everything-mean">What is going on? What does everything mean?
      </a>
      <ul>
        <li><a href="#gitjava">Git.java</a></li>
        <li><a href="#index.java">Index.java</a></li>
        <li><a href="#head.java">Head.java</a></li>
        <li><a href="#objects.java">Objects.java</a></li>
        <li><a href="#IlleGITimate.java">IlleGITimate.java</a></li>
      </ul>
    </li>
    <li>
      <a href="#stretch-goals">Stretch Goals</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#tester">Tester</a></li>
    <li><a href="#tips">Tips</a></li>
    <li><a href="#how-do-i-run-the-code">How do I run the code?</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## What is going on? What does everything mean?

Hey, it's Miles. Please read this entire thing (on Github, not VSCode, so you get the nice formatting) so that you have a good idea about how IlleGITimate works. I probably haven't done things in the best way so the purpose of this README is to give you a good idea of how I thought about coding this project thus far. Hopefully this means you will have an easier time making changes! 

The codebase of this project is broken up into individual `.java` files which each represent a core component of our Git recreation. **Each class, at it's core, is a file path wrapped with extra functionality.** Wrapping means each class contains a private `File` variable which it encloses:

- `Git.java` represents the folder `git`
- `Objects.java` represents the folder `git/objects`
- `Index.java` represents the file `git/index`
- `Head.java` represents the file `git/HEAD`

This means if you want to change something about the way the `git/objects` folder works, you should probably go to `Objects.java`. 

The purpose of `IlleGITimate.java` is to coordinate all these classes and define methods like 

```java
public void commitFile(File file) throws IOException {
    // example code
    index.doSomething(file);
    objects.doSomething(file);
} 
```

which use methods from multiple classes to get things done. For each class, I'll point out some important methods that I've made and also the general methodology I had when writing it. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## ```Git.java```

There's nothing really going on here, so this is a good time for me to go over what the basic methods you'll see in *every* class are. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.

- npm
  ```sh
  npm install npm@latest -g
  ```

### Installation

_Below is an example of how you can instruct your audience on installing and setting up your app. This template doesn't rely on any external dependencies or services._

1. Get a free API Key at [https://example.com](https://example.com)
2. Clone the repo

   ```java

   ```

3. Install NPM packages
   ```sh
   npm install
   ```
4. Enter your API in `config.js`
   ```js
   const API_KEY = "ENTER YOUR API";
   ```
5. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->

## Roadmap

- [x] Add Changelog
- [x] Add back to top links
- [ ] Add Additional Templates w/ Examples
- [ ] Add "components" document to easily copy & paste sections of the readme
- [ ] Multi-language Support
  - [ ] Chinese
  - [ ] Spanish

See the [open issues](https://github.com/othneildrew/Best-README-Template/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the Unlicense License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

Your Name - [@your_twitter](https://twitter.com/your_username) - email@example.com

Project Link: [https://github.com/your_username/repo_name](https://github.com/your_username/repo_name)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

Use this space to list resources you find helpful and would like to give credit to. I've included a few of my favorites to kick things off!

- [Choose an Open Source License](https://choosealicense.com)
- [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
- [Malven's Flexbox Cheatsheet](https://flexbox.malven.co/)
- [Malven's Grid Cheatsheet](https://grid.malven.co/)
- [Img Shields](https://shields.io)
- [GitHub Pages](https://pages.github.com)
- [Font Awesome](https://fontawesome.com)
- [React Icons](https://react-icons.github.io/react-icons/search)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


const ButtonWithLoading = withLoading(Button);

this.setState(updateSearchTopStoriesState(hits, page));


<div className="interactions">
          <ButtonWithLoading
            isLoading={isLoading}
            onClick={() => this.fetchSearchTopStories(searchKey, page + 1)}>
            More
        </ButtonWithLoading>


const updateSearchTopStoriesState = (hits, page) => (prevState) => {
    const { searchKey, results } = prevState;
  
    const oldHits = results && results[searchKey]
      ? results[searchKey].hits
      : [];
  
    const updatedHits = [
      ...oldHits,
      ...hits
    ];
  
    
    return {
      results: {
        ...results,
        [searchKey]: { hits: updatedHits, page }
      },
      isLoading: false
    };
  };


  const Loading = () =>
  <div>Loading ...</div>

const withLoading = (Component) => ({ isLoading, ...rest }) =>
  isLoading
    ? <Loading />
    : <Component {...rest} />
